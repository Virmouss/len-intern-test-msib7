package com.example.internLEN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.internLEN.service.BukuServiceImpl;
import com.example.internLEN.service.MahasiswaServiceImpl;
import com.example.internLEN.service.PinjamanServiceImpl;

import io.grpc.Server;
import io.grpc.ServerBuilder;

@SpringBootApplication
public class InternLenApplication implements CommandLineRunner {

	@Autowired
    private BukuServiceImpl bukuService;
	@Autowired
	private MahasiswaServiceImpl mhsService;
	@Autowired
	private PinjamanServiceImpl pinjamService;

	public static void main(String[] args) {
		SpringApplication.run(InternLenApplication.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
        Server server = ServerBuilder.forPort(9090)
                .addService(bukuService)
				.addService(mhsService)
				.addService(pinjamService)
                .build()
                .start();

        System.out.println("gRPC server started on port 9090");

    }
}
