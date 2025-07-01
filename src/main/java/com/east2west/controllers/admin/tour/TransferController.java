package com.east2west.controllers.admin.tour;


import com.east2west.models.DTO.ModelResponse;
import com.east2west.models.DTO.TransferDTO;
import com.east2west.models.Entity.Transfer;
import com.east2west.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tours/transfers")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }



    // Endpoint: Create Transfers
    @PostMapping
    public ResponseEntity<ModelResponse<TransferDTO>> createTransfer(@RequestBody TransferDTO transfer) {
        try {
            Optional<Transfer> transfers = transferService.findByTransferName(transfer.getTransfername());

            if(transfers.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<TransferDTO>builder()
                                .status(400)
                                .message("Transfer name " + transfer.getTransfername() + " already exists.")
                                .data(null)
                                .build()
                );
            }
            TransferDTO data = transferService.createTransfer(transfer);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<TransferDTO>builder()
                            .status(201)
                            .message(data.getTransfername() + " transfer created successfully !!!")
                            .data(data)
                            .build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ModelResponse.<TransferDTO>builder()
                            .status(400)
                            .message("Error Internal Server !!!")
                            .data(null)
                            .build()
            );
        }
    }


    // Endpoint: Delete transfer by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ModelResponse<Transfer>> deleteTransfer(@PathVariable int id) {
        try {
            String data = transferService.deleteTransfer(id);
            if(data.startsWith("Deleted")){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Transfer>builder()
                                .status(200)
                                .message(data)
                                .data(null)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Transfer>builder()
                                .status(404)
                                .message(data)
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Transfer>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR !!!")
                            .data(null)
                            .build()
            );

        }
    }

    // Endpoint: Pagination
    @GetMapping
    public Page<TransferDTO> getTransfers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transferService.getAllTransfers(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transferid")));
    }

    // Endpoint: Search transfer
    @GetMapping("/search")
    public List<TransferDTO> searchTransfer(@RequestParam String keyword) {
        return transferService.searchTransfer(keyword);
    }


    // Endpoint: Get a transfer by id
    @GetMapping("/{id}")
    public ResponseEntity<ModelResponse<Optional<TransferDTO>>> getTransferById(@PathVariable int id){
        try {
            Optional<TransferDTO> data = transferService.getTransferById(id);
            if(data.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<Optional<TransferDTO>>builder()
                                .status(200)
                                .message("OK")
                                .data(data)
                                .build()
                );
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<Optional<TransferDTO>>builder()
                                .status(404)
                                .message("Not found transfer !!!")
                                .data(null)
                                .build()
                );
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<Optional<TransferDTO>>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );
        }

    }



    // Endpoint: Update transfer
    @PutMapping
    public ResponseEntity<ModelResponse<TransferDTO>> updateTransfer(@RequestBody TransferDTO transfer) {
        try {
            TransferDTO data = transferService.updateTransfer(transfer);
            if(data != null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        ModelResponse.<TransferDTO>builder()
                                .status(200)
                                .message(data.getTransfername() +" transfer updated successfully !!!")
                                .data(data)
                                .build()
                );
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ModelResponse.<TransferDTO>builder()
                                .status(404)
                                .message("Transfer not found !!!")
                                .data(null)
                                .build()
                );
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<TransferDTO>builder()
                            .status(500)
                            .message("INTERNAL SERVER ERROR")
                            .data(null)
                            .build()
            );

        }
    }


    // Endpoint: Create transfer  by file csv
    @PostMapping("/uploadfromcsv")
    public ResponseEntity<ModelResponse<?>> uploadFile(@RequestParam("file") MultipartFile[] file) {
        try {

            String data = transferService.saveTransferFromCSV(file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ModelResponse.<TransferDTO>builder()
                            .status(201)
                            .message("Create successfully " + data +" transfer !!!").data(null).build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<TransferDTO>builder()
                            .status(500)
                            .message(String.valueOf(e)).data(null).build());
        }
    }




    // Endpoint: List transfer
    @GetMapping("/list")
    public ResponseEntity<ModelResponse<List<TransferDTO>>> listTransfer(){
        try{
            List<TransferDTO> data = transferService.getAllTransfer();
            return ResponseEntity.status(HttpStatus.OK).body(
                    ModelResponse.<List<TransferDTO>>builder().status(200).message("OK").data(data).build()
            );
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ModelResponse.<List<TransferDTO>>builder().status(200).message("INTERNAL SERVER ERROR").data(null).build()
            );
        }
    }

}
