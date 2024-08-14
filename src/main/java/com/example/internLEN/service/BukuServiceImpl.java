package com.example.internLEN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.internLEN.Buku.AddBukuRequest;
import com.example.internLEN.Buku.BukuIdRequest;
import com.example.internLEN.Buku.BukuResponse;
import com.example.internLEN.Buku.EmptyResponse;
import com.example.internLEN.Buku.UpdateBukuRequest;
import com.example.internLEN.BukuServiceGrpc;
import com.example.internLEN.entity.Buku;
import com.example.internLEN.repository.BukuRepo;

import io.grpc.stub.StreamObserver;

@Service
public class BukuServiceImpl extends BukuServiceGrpc.BukuServiceImplBase {

    @Autowired
    private BukuRepo bukuRepo;

    @Override
    public void addBuku(AddBukuRequest request, StreamObserver<BukuResponse> responseObserver) {
        Buku buku = new Buku();
        buku.setJudul(request.getTitle());
        buku.setPenulis(request.getPenulis());
        buku.setKuantitas(request.getKuantitas());
        buku.setTempatPenyimpanan(request.getTempatPenyimpanan());
        
        Buku savedBuku = bukuRepo.save(buku);
        
        BukuResponse response = BukuResponse.newBuilder()
                .setId(savedBuku.getId())
                .setTitle(savedBuku.getJudul())
                .setPenulis(savedBuku.getPenulis())
                .setKuantitas(savedBuku.getKuantitas())
                .setTempatPenyimpanan(savedBuku.getTempatPenyimpanan())
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getBuku(BukuIdRequest request, StreamObserver<BukuResponse> responseObserver) {
        Long id = Long.parseLong(request.getId());
        Buku buku = bukuRepo.findById(id).orElse(null);
        
        if (buku == null) {
            responseObserver.onError(new IllegalArgumentException("Buku Tidak Ditemukan"));
            return;
        }
        
        BukuResponse response = BukuResponse.newBuilder()
                .setId(buku.getId())
                .setTitle(buku.getJudul())
                .setPenulis(buku.getPenulis())
                .setKuantitas(buku.getKuantitas())
                .setTempatPenyimpanan(buku.getTempatPenyimpanan())
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBuku(UpdateBukuRequest request, StreamObserver<BukuResponse> responseObserver) {
        Long id = request.getId();
        Buku buku = bukuRepo.findById(id).orElse(null);
        
        if (buku == null) {
            responseObserver.onError(new IllegalArgumentException("Buku Tidak Ditemukan"));
            return;
        }
        
        buku.setJudul(request.getTitle());
        buku.setPenulis(request.getPenulis());
        buku.setKuantitas(request.getKuantitas());
        buku.setTempatPenyimpanan(request.getTempatPenyimpanan());
        
        Buku updatedBuku = bukuRepo.save(buku);
        
        BukuResponse response = BukuResponse.newBuilder()
                .setId(updatedBuku.getId())
                .setTitle(updatedBuku.getJudul())
                .setPenulis(updatedBuku.getPenulis())
                .setKuantitas(updatedBuku.getKuantitas())
                .setTempatPenyimpanan(updatedBuku.getTempatPenyimpanan())
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBuku(BukuIdRequest request, StreamObserver<EmptyResponse> responseObserver) {
        Long id = Long.parseLong(request.getId());
        boolean exists = bukuRepo.existsById(id);
        
        if (!exists) {
            responseObserver.onError(new IllegalArgumentException("Buku not found"));
            return;
        }
        
        bukuRepo.deleteById(id);
        
        EmptyResponse response = EmptyResponse.newBuilder()
                .setSuccess(true)
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
