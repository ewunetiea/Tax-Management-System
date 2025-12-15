package com.tms.Maker.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tms.Admin.Entity.User;
import com.tms.Common.RecentActivity.RecentActivity;
import com.tms.Common.RecentActivity.RecentActivityMapper;
import com.tms.Maker.entity.MakerSearchPayload;
import com.tms.Maker.entity.Tax;
import com.tms.Maker.entity.TaxFile;
import com.tms.Maker.mapper.TaxFileMapper;
import com.tms.Maker.mapper.TaxableMapper;

@Service

public class TaxableService {

    @Autowired
    private TaxableMapper taxableMapper;

    @Autowired
    private TaxFileMapper taxFileMapper;

    @Autowired

    private RecentActivityMapper recentActivityMapper;

    RecentActivity recentActivity = new RecentActivity();

    User user = new User();

    @Value("${file.storage.root}")
    private String rootPath;

    // String uploadDir = "\\\\10.10.101.76\\fileUploadFolder"; // Use IP upload

    private String uploadDir; // Initialize in @PostConstruct

    @PostConstruct
    public void init() {
        uploadDir = Paths.get(rootPath, "taxFile").toString();
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Transactional
    public Tax createTaxWithFiles(Tax tax, MultipartFile[] files) throws IOException {

        Tax taxResponse = new Tax();

        String mainGuid = generateGuid();

        if (files != null && files.length > 0 && tax.getTaxFile() != null) {

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Check for existing files in the database
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // Check if the file already exists in the database
                    if (taxFileMapper.checkFilnameExistance(file.getOriginalFilename())) {
                        tax.setFileExsistance("Exists");
                        // Notify user that the file already exists
                        return tax;
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

                    file.transferTo(destination);

                    String fileId = generateGuid();
                    tf.setId(mainGuid);
                    tf.setSupportId(fileId);
                    tf.setFileName(file.getOriginalFilename());

                    taxFileMapper.insertFile(tf);

                    recentActivity
                            .setMessage("Tax  with Reference number  " + tax.getReference_number() + " is created");
                    user.setId(tax.getUser_id());
                    recentActivity.setUser(user);
                    recentActivityMapper.addRecentActivity(recentActivity);

                }
            }

            taxResponse = taxableMapper.fetchTaxById(tax_id);
            taxResponse.setFileExsistance("notExist");

        }

        return taxResponse;
    }

    public String generateGuid() {
        UUID uuid = UUID.randomUUID(); // Generate a random UUID
        return uuid.toString().toUpperCase(); // Convert to string and make it uppercase
    }

    // @Transactional
    public Tax updateTax(Tax tax, MultipartFile[] files) throws IOException {

        try {

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            taxableMapper.updateTaxable(tax);

            if (tax.getIsFileEdited()) {

                for (TaxFile taxFileToDelete : tax.getPreviouseTaxFile()) {
                    // Delete from DB
                    taxFileMapper.deleteTaxFile(taxFileToDelete.getFileName());

                    // Delete from folder
                    File existingFile = new File(dir, taxFileToDelete.getFileName());
                    if (existingFile.exists()) {
                        existingFile.delete();

                    }
                }

                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    TaxFile tf = tax.getTaxFile().get(i);
                    tf.setTax_id(tax.getId());

                    if (!file.isEmpty()) {
                        String fileName = file.getOriginalFilename();
                        File destination = new File(dir, fileName);

                        file.transferTo(destination);
                        String fileId = generateGuid();
                        tf.setSupportId(fileId);
                        tf.setFileName(fileName);
                        taxFileMapper.insertFile(tf);

                    }
                }
            }
            // ✅ Log recent activity

            user.setId(tax.getUser_id());
            recentActivity.setUser(user);
            recentActivity.setMessage("Tax with Reference number " + tax.getReference_number() + " updated");
            recentActivityMapper.addRecentActivity(recentActivity);

            return taxableMapper.fetchTaxById(tax.getId());

        } catch (Exception e) {
            return null;
            // TODO: handle exception
        }

    }

    public Tax fetchTaxById(int id) {

        return taxableMapper.fetchTaxBiID(id);

    }

    @Transactional

    public List<Tax> fetchTaxBasedonStatus(MakerSearchPayload payload) {

        Map<String, Integer> statusMap = new HashMap<>();
        statusMap.put("drafted", 6);
        statusMap.put("submited", 0);
        statusMap.put("sent", 1);

        statusMap.put("setteled", 5);
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

    public void deleteTax(Tax tax, Long user_id) {

        List<TaxFile> files = tax.getTaxFile();

        // 2. Delete Tax record from DB it deletes FileDetailClaim table because of
        // CASCADE
        taxableMapper.deleteTaxById(tax.getId());

        if (files != null && !files.isEmpty()) {
            // from other server

            for (TaxFile taxFile : files) {

                File fileToDelete = new File(uploadDir, taxFile.getFileName());

                if (fileToDelete.exists()) {
                    fileToDelete.delete();

                } else {
                }
            }
        } else {
        }

        user.setId(user_id);
        recentActivity.setUser(user);
        recentActivity.setMessage("Tax with Reference number " +
                tax.getReference_number() + " deleted");
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    @Transactional
    public void submitTaxToBranchManger(Tax tax, Long user_id) { // set status to 0 or waiting state

        taxableMapper.submitToBrancManager(tax.getId());
        user.setId(user_id);
        recentActivity.setUser(user);
        recentActivity.setMessage("Tax with Reference number " +
                tax.getReference_number() + " submited to checker");
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public void backTaxToDraftedState(Tax tax , Long user_id) { // set status to 6

        int status = taxableMapper.fetchTaxByID(tax.getId());

        if (status == 0 || status == 2 || status == 3) {
            taxableMapper.backToDraftedState(tax.getId());

               user.setId(user_id);
        recentActivity.setUser(user);
        recentActivity.setMessage("Tax with Reference number " +
                tax.getReference_number() + " back to drafted state");
        recentActivityMapper.addRecentActivity(recentActivity);

        }
    }

    

}
