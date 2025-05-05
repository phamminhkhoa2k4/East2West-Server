package com.east2west.service;


import com.east2west.models.DTO.TransferDTO;
import com.east2west.models.Entity.Transfer;
import com.east2west.models.mapper.TransferMapper;
import com.east2west.repository.TransferRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {
    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }




    public Optional<Transfer> findByTransferName(String transferName){
        return transferRepository.findByTransfername(transferName);
    }

    public TransferDTO createTransfer(TransferDTO transfer) {
        return TransferMapper.INSTANCE.toDTO(transferRepository.save(
                Transfer.builder()
                        .transferid(transfer.getTransferid())
                        .transfername(transfer.getTransfername())
                        .transferthumbnail(transfer.getTransferthumbnail())
                        .transferduration(transfer.getTransferduration())
                        .description(transfer.getDescription())
                        .build()
        ));
    }


    public String deleteTransfer(int id){

        Optional<Transfer> meals = transferRepository.findById(id);
        if(meals.isPresent()){
            transferRepository.deleteById(id);
            return "Deleted " + meals.get().getTransfername() + " transfer successfully";
        }else{
            return "Not found transfer";
        }
    }

    public Page<TransferDTO> getAllTransfers(Pageable pageable) {
        Page<Transfer> transferPage = transferRepository.findAll(pageable);
        return transferPage.map(TransferMapper.INSTANCE::toDTO);
    }

    public List<TransferDTO> searchTransfer(String keyword) {
        List<Transfer> transferList = transferRepository.findByTransfernameContainingIgnoreCase(keyword);
        return transferList.stream().map(TransferMapper.INSTANCE::toDTO).toList();
    }


    public Optional<TransferDTO> getTransferById(int id) {
        Optional<Transfer> transfer = transferRepository.findById(id);
        if (transfer.isPresent()){
            TransferDTO transferDTO  = TransferMapper.INSTANCE.toDTO(transfer.get());
            return Optional.ofNullable(transferDTO);
        }
        return Optional.empty();
    }


    public TransferDTO updateTransfer(TransferDTO transfer){
        Optional<Transfer> transfers =  transferRepository.findById(transfer.getTransferid());
        if(transfers.isPresent()){
            Transfer data = TransferMapper.INSTANCE.toEntity(transfer);
            return TransferMapper.INSTANCE.toDTO(transferRepository.save(data));
        }
        return null;
    }



    public String saveTransferFromCSV(@NotNull MultipartFile[] files) {
        if (files.length == 0) {
            return "Chưa chọn file!";
        }

        try {
            List<Transfer> transfersList = new ArrayList<>();
            Optional<Transfer> maxIdTransfer = transferRepository.findAll().stream()
                    .max(Comparator.comparingInt(Transfer::getTransferid));
            int idCounter = maxIdTransfer.map(transfer -> transfer.getTransferid() + 1)
                    .orElse(1);

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] data = line.split(",");
                    if (data.length < 4) {
                        continue;
                    }



                    String TransferThumbnail = data[0].trim();
                    String TransferName = data[1].trim();
                    String TransferDuration = data[2].trim();
                    String TransferDescription = data[3].trim();
                    System.out.println("Đang xử lý tiện ích: " + TransferName);

                    Transfer transfer = new Transfer();
                    transfer.setTransferid(idCounter++);
                    transfer.setTransfername(TransferName);
                    transfer.setTransferthumbnail(TransferThumbnail);
                    transfer.setTransferduration(TransferDuration);
                    transfer.setDescription(TransferDescription);
                    transfersList.add(transfer);
                }
            }


            transferRepository.saveAll(transfersList);
            return "" + transfersList.size();
        } catch (Exception e) {
            return "Lỗi xử lý file: " + e.getMessage();
        }
    }
}
