package com.example.internLEN.controller;

import com.example.internLEN.Pinjaman.EmptyRequest;
import com.example.internLEN.Pinjaman.PinjamanListResponse;
import com.example.internLEN.PinjamanServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.List;

import com.example.internLEN.Pinjaman.PinjamanResponse;

@RestController
@RequestMapping("/api/pinjaman")
public class PinjamanController {

    private final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();
    private final PinjamanServiceGrpc.PinjamanServiceBlockingStub stub = PinjamanServiceGrpc.newBlockingStub(channel);
    
    @GetMapping
    public List<PinjamanResponse> getAllPinjaman() {
        EmptyRequest request = EmptyRequest.newBuilder().build();
        PinjamanListResponse response = stub.listPinjaman(request);
        return response.getPinjamanList().stream().collect(Collectors.toList());
    }
}
