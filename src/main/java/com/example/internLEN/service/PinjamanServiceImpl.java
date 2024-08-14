package com.example.internLEN.service;

import com.example.internLEN.entity.Buku;
import com.example.internLEN.entity.Mahasiswa;
import com.example.internLEN.entity.Pinjaman;
import com.example.internLEN.repository.BukuRepo;
import com.example.internLEN.repository.MahasiswaRepo;
import com.example.internLEN.repository.PinjamanRepo;

import com.example.internLEN.PinjamanServiceGrpc;
import com.example.internLEN.Pinjaman.AddPinjamanRequest;
import com.example.internLEN.Pinjaman.UpdatePinjamanRequest;
import com.example.internLEN.Pinjaman.PinjamanIdRequest;
import com.example.internLEN.Pinjaman.PinjamanResponse;
import com.example.internLEN.Pinjaman.PinjamanListResponse;
import com.example.internLEN.Pinjaman.CalculateDendaRequest;
import com.example.internLEN.Pinjaman.DendaResponse;
import com.example.internLEN.Pinjaman.EmptyResponsePinjam;
import com.example.internLEN.Pinjaman.EmptyRequest;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class PinjamanServiceImpl extends PinjamanServiceGrpc.PinjamanServiceImplBase {

    @Autowired
    private PinjamanRepo pinjamanRepo;

    @Autowired
    private BukuRepo bukuRepo;

    @Autowired
    private MahasiswaRepo mahasiswaRepo;

    @Override
    public void addPinjaman(AddPinjamanRequest request, StreamObserver<PinjamanResponse> responseObserver) {
        Optional<Buku> bukuOpt = bukuRepo.findById(request.getBukuId());
        Optional<Mahasiswa> mahasiswaOpt = mahasiswaRepo.findById(request.getMahasiswaId());

        if (!bukuOpt.isPresent() || !mahasiswaOpt.isPresent()) {
            responseObserver.onError(new IllegalArgumentException("Invalid buku or mahasiswa ID"));
            return;
        }

        Pinjaman pinjaman = new Pinjaman();
        pinjaman.setBuku(bukuOpt.get());
        pinjaman.setMahasiswa(mahasiswaOpt.get());
        pinjaman.setTglPinjam(LocalDate.parse(request.getTglPinjam()));
        pinjaman.setTglBatasanPengembalian(LocalDate.parse(request.getTglBatasanPengembalian()));

        Pinjaman savedPinjaman = pinjamanRepo.save(pinjaman);

        PinjamanResponse response = PinjamanResponse.newBuilder()
                .setId(savedPinjaman.getId())
                .setMahasiswaId(savedPinjaman.getMahasiswa().getId())
                .setBukuId(savedPinjaman.getBuku().getId())
                .setTglPinjam(savedPinjaman.getTglPinjam().toString())
                .setTglBatasanPengembalian(savedPinjaman.getTglBatasanPengembalian().toString())
                .setTglPengembalian(savedPinjaman.getTglPengembalian() != null ? savedPinjaman.getTglPengembalian().toString() : "")
                .setDenda(savedPinjaman.getDenda() != null ? savedPinjaman.getDenda().toString() : "")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updatePinjaman(UpdatePinjamanRequest request, StreamObserver<EmptyResponsePinjam> responseObserver) {
    Optional<Pinjaman> pinjamanOpt = pinjamanRepo.findById(request.getId());

    if (!pinjamanOpt.isPresent()) {
        responseObserver.onError(new IllegalArgumentException("Pinjaman not found"));
        return;
    }

    Pinjaman pinjaman = pinjamanOpt.get();
    pinjaman.setMahasiswa(mahasiswaRepo.findById(request.getMahasiswaId()).orElse(pinjaman.getMahasiswa()));
    pinjaman.setBuku(bukuRepo.findById(request.getBukuId()).orElse(pinjaman.getBuku()));
    pinjaman.setTglPinjam(LocalDate.parse(request.getTglPinjam()));
    pinjaman.setTglBatasanPengembalian(LocalDate.parse(request.getTglBatasanPengembalian()));
    

    if (!request.getTglPengembalian().isEmpty()) {
        pinjaman.setTglPengembalian(LocalDate.parse(request.getTglPengembalian()));
    }
    
    if (!request.getDenda().isEmpty()) {
        pinjaman.setDenda(new BigDecimal(request.getDenda()));
    }

    pinjamanRepo.save(pinjaman);

    EmptyResponsePinjam response = EmptyResponsePinjam.newBuilder().setSuccess(true).build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
}

    @Override
    public void deletePinjaman(PinjamanIdRequest request, StreamObserver<EmptyResponsePinjam> responseObserver) {
        pinjamanRepo.deleteById(request.getId());
        EmptyResponsePinjam response = EmptyResponsePinjam.newBuilder().setSuccess(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listPinjaman(EmptyRequest request, StreamObserver<PinjamanListResponse> responseObserver) {
        Iterable<Pinjaman> pinjamanList = pinjamanRepo.findAll();
        PinjamanListResponse.Builder responseBuilder = PinjamanListResponse.newBuilder();

        for (Pinjaman pinjaman : pinjamanList) {
            PinjamanResponse response = PinjamanResponse.newBuilder()
                    .setId(pinjaman.getId())
                    .setMahasiswaId(pinjaman.getMahasiswa().getId())
                    .setBukuId(pinjaman.getBuku().getId())
                    .setTglPinjam(pinjaman.getTglPinjam().toString())
                    .setTglBatasanPengembalian(pinjaman.getTglBatasanPengembalian().toString())
                    .setTglPengembalian(pinjaman.getTglPengembalian() != null ? pinjaman.getTglPengembalian().toString() : "")
                    .setDenda(pinjaman.getDenda() != null ? pinjaman.getDenda().toString() : "")
                    .build();
            responseBuilder.addPinjaman(response);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void calculateDenda(CalculateDendaRequest request, StreamObserver<DendaResponse> responseObserver) {
        Optional<Pinjaman> pinjamanOpt = pinjamanRepo.findById(request.getPinjamanId());

        if (!pinjamanOpt.isPresent()) {
            responseObserver.onError(new IllegalArgumentException("Pinjaman Tidak Ada"));
            return;
        }

        Pinjaman pinjaman = pinjamanOpt.get();
        BigDecimal denda = BigDecimal.ZERO;

        if (pinjaman.getTglPengembalian() != null) {
            long hariTelat = ChronoUnit.DAYS.between(pinjaman.getTglBatasanPengembalian(), pinjaman.getTglPengembalian());

            if (hariTelat > 0) {
                
                long hariDenda = hariTelat;
                int jmlhDenda = 1000; 

                if (hariDenda > 2) {
                    denda = new BigDecimal(2 * jmlhDenda);
                    hariDenda -= 2;
                    jmlhDenda += 1000; 
                } else {
                    denda = new BigDecimal(hariDenda * jmlhDenda);
                    hariDenda = 0;
                }

                while (hariDenda > 0) {
                    if (hariDenda > 2) {
                        denda = denda.add(new BigDecimal(2 * jmlhDenda));
                        hariDenda -= 2;
                    } else {
                        denda = denda.add(new BigDecimal(hariDenda * jmlhDenda));
                        hariDenda = 0;
                    }
                    jmlhDenda += 1000;
                }
            }
        }

        pinjaman.setDenda(denda);
        pinjamanRepo.save(pinjaman);

        DendaResponse response = DendaResponse.newBuilder().setDenda(denda.toString()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
