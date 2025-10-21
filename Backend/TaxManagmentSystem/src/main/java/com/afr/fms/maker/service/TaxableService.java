package com.afr.fms.Maker.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;
import com.afr.fms.Maker.entity.Tax;
import com.afr.fms.Maker.entity.TaxFile;
import com.afr.fms.Maker.mapper.TaxFileMapper;
import com.afr.fms.Maker.mapper.TaxableMapper;
import com.afr.fms.Payload.payload.Payload;

import org.springframework.core.io.Resource;

@Service

public class TaxableService {

    @Autowired
    private TaxableMapper taxableMapper;

    @Autowired
    private TaxFileMapper taxFileMapper;

    @Autowired

    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();

    @Transactional
    public String createTaxWithFiles(Tax tax, MultipartFile[] files) throws IOException {
        String mainGuid = generateGuid();

        if (files != null && files.length > 0 && tax.getTaxFile() != null) {
            String uploadDir = Paths.get(System.getProperty("user.dir"), "taxFiles").toString();
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Check for existing files in the database
            for (MultipartFile file : files) {
                System.out.println("Checking file: " + file.getOriginalFilename());
                if (!file.isEmpty()) {
                    // Check if the file already exists in the database
                    if (taxFileMapper.checkFilnameExistance(file.getOriginalFilename())) {
                        // Notify user that the file already exists
                        return "Exists";
                    }
                }
            }

            tax.setMainGuid(mainGuid);
            tax.setReference_number(generateReferenceNumber());

            // Create tax entry
            Long tax_id = taxableMapper.createTax(tax);

            // Process and store the files
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                TaxFile tf = tax.getTaxFile().get(i);
                tf.setTax_id(tax_id);

                if (!file.isEmpty()) {
                    File destination = new File(dir, file.getOriginalFilename());

                    // Transfer the file to the destination
                    file.transferTo(destination);

                    // Generate ID and update the taxFile object
                    String fileId = generateGuid();
                    tf.setId(mainGuid);
                    tf.setSupportId(fileId);
                    tf.setFileName(file.getOriginalFilename());

                    // Insert the file record in the database
                    taxFileMapper.insertFile(tf);

                    User user = new User();
                    recentActivity
                            .setMessage("Tax  with Reference number  " + tax.getReference_number() + " is created");
                    user.setId(tax.getUser_id());
                    recentActivity.setUser(user);
                    recentActivityMapper.addRecentActivity(recentActivity);

                }
            }
        }

        return mainGuid;
    }

    public String generateGuid() {
        UUID uuid = UUID.randomUUID(); // Generate a random UUID
        return uuid.toString().toUpperCase(); // Convert to string and make it uppercase
    }

    @Transactional
    public void updateTax(Tax taxable) {

        taxableMapper.createTax(null);

    }

    public Tax fetchTaxById(int id) {

        return taxableMapper.fetchTaxBiID(id);

    }

    // @Transactional(readOnly = true)

    public List<Tax> fetchTaxBasedonStatus(Payload payload) {

        System.out.println("_______________FF_______________________");
        System.out.println(payload);

        Map<String, Integer> statusMap = new HashMap<>();
        statusMap.put("drafted", 6);
        statusMap.put("submited", 0);
        statusMap.put("approved", 1);
        statusMap.put("rejected", 2);

        for (String control : statusMap.keySet()) {
            if (payload.getRouteControl().contains(control)) {
                payload.setStatus(statusMap.get(control));
                break; // Exit the loop once a match is found
            }
        }

        return taxableMapper.fetchTaxBasedonStatus(payload);
    }

    private String generateReferenceNumber() {
        String lastRef = taxableMapper.getLastReferenceNumber();
        if (lastRef == null || lastRef.isEmpty()) {
            return "TAX1";
        }

        // Extract numeric part — e.g., TAX12 → 12
        String numberPart = lastRef.replaceAll("[^0-9]", "");
        int nextNumber = 1;
        try {
            nextNumber = Integer.parseInt(numberPart) + 1;
        } catch (NumberFormatException e) {
            // fallback if number part is invalid
            nextNumber = 1;
        }

        return "TAX" + nextNumber;
    }

    public void deleteTax(Long id) {

        taxableMapper.deleteTaxById(id);
    }

    public void submitTaxToBranchManger(Long id) { // set status to 0 or waiting state

        taxableMapper.submitToBrancManager(id);
    }

    public void backTaxToDraftedState(Long id) { // set status to 6

        taxableMapper.backToDraftedState(id);
    }

    private final String folderPath = Paths.get(System.getProperty("user.dir"), "taxFiles").toString();

    public File getFileByFileName(String fileName) throws FileNotFoundException {
        File file = new File(folderPath, fileName);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        return file;
    }

    public Resource getFileResource(String fileName) throws FileNotFoundException {
        File file = getFileByFileName(fileName);
        return new FileSystemResource(file);
    }

}
