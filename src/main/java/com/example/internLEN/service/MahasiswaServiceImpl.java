package com.example.internLEN.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.internLEN.Mahasiswa.AddMahasiswaRequest;
import com.example.internLEN.Mahasiswa.EmptyResponseMhs;
import com.example.internLEN.Mahasiswa.MahasiswaIdRequest;
import com.example.internLEN.Mahasiswa.MahasiswaResponse;
import com.example.internLEN.Mahasiswa.UpdateMahasiswaRequest;
import com.example.internLEN.MahasiswaServiceGrpc;
import com.example.internLEN.entity.Mahasiswa;
import com.example.internLEN.repository.MahasiswaRepo;

import io.grpc.stub.StreamObserver;

@Service
public class MahasiswaServiceImpl extends MahasiswaServiceGrpc.MahasiswaServiceImplBase {

    @Autowired
    private MahasiswaRepo mahasiswaRepo;

    @Override
    public void addMahasiswa(AddMahasiswaRequest request, StreamObserver<MahasiswaResponse> responseObserver) {
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNama(request.getNama());
        mahasiswa.setNim(request.getNim());
        mahasiswa.setJurusan(request.getJurusan());

        Mahasiswa savedMahasiswa = mahasiswaRepo.save(mahasiswa);

        MahasiswaResponse response = MahasiswaResponse.newBuilder()
                .setId(savedMahasiswa.getId())
                .setNama(savedMahasiswa.getNama())
                .setNim(savedMahasiswa.getNim())
                .setJurusan(savedMahasiswa.getJurusan())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMahasiswa(MahasiswaIdRequest request, StreamObserver<MahasiswaResponse> responseObserver) {
        Long id = Long.parseLong(request.getId());
        Mahasiswa mahasiswa = mahasiswaRepo.findById(id).orElse(null);

        if (mahasiswa == null) {
            responseObserver.onError(new RuntimeException("Mahasiswa Tidak Ditemukan"));
            return;
        }

        MahasiswaResponse response = MahasiswaResponse.newBuilder()
                .setId(mahasiswa.getId())
                .setNama(mahasiswa.getNama())
                .setNim(mahasiswa.getNim())
                .setJurusan(mahasiswa.getJurusan())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateMahasiswa(UpdateMahasiswaRequest request, StreamObserver<MahasiswaResponse> responseObserver) {
        Long id = request.getId();
        Mahasiswa mahasiswa = mahasiswaRepo.findById(id).orElse(null);

        if (mahasiswa == null) {
            responseObserver.onError(new RuntimeException("Mahasiswa Tidak Ditemukan"));
            return;
        }

        mahasiswa.setNama(request.getNama());
        mahasiswa.setNim(request.getNim());
        mahasiswa.setJurusan(request.getJurusan());

        Mahasiswa updatedMahasiswa = mahasiswaRepo.save(mahasiswa);

        MahasiswaResponse response = MahasiswaResponse.newBuilder()
                .setId(updatedMahasiswa.getId())
                .setNama(updatedMahasiswa.getNama())
                .setNim(updatedMahasiswa.getNim())
                .setJurusan(updatedMahasiswa.getJurusan())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteMahasiswa(MahasiswaIdRequest request, StreamObserver<EmptyResponseMhs> responseObserver) {
        Long id = Long.parseLong(request.getId());
        boolean exists = mahasiswaRepo.existsById(id);
        
        if (!exists) {
            responseObserver.onError(new IllegalArgumentException("Buku not found"));
            return;
        }
        
        mahasiswaRepo.deleteById(id);
        
        EmptyResponseMhs response = EmptyResponseMhs.newBuilder()
                .setSuccess(true)
                .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
