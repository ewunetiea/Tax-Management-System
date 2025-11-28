package com.tms.Common.Permission.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.Admin.Entity.User;
import com.tms.Common.Entity.Functionalities;
import com.tms.Common.Permission.Mapper.UserFunctionalityMapper;

@Service
public class UserFunctionalityService {

    @Autowired
    private UserFunctionalityMapper userFunctionalityMapper;

    public void createUserFunctionality(User user) {
        for (Functionalities functionality : user.getFunctionalities()) {
            userFunctionalityMapper.deactivateUserFunctionality(user.getId(), functionality.getId(), false);
        }
    }

    public List<User> generatedUsers(User user) {

        return userFunctionalityMapper.generatedUsers(user.getFirst_name(), user.getMiddle_name(), user.getLast_name(),
                user.getEmail(), user.getPhone_number(),
                user.getBranch() != null ? user.getBranch().getId() : null,
                user.getRegion() != null ? user.getRegion().getId() : null,
                user.getJobPosition() != null ? user.getJobPosition().getId() : null, user.getGender(),
                user.getEmployee_id());
    }

    public void deleteUserFunctionality(List<Functionalities> functionalities) {

        try {
              System.out.println(functionalities);
        for (Functionalities functionality : functionalities) {
            userFunctionalityMapper.deleteUserFunctionalityBYId(functionality.getId());
        }
            
        } catch (Exception e) {
            
            System.out.println(e);
            // TODO: handle exception
        }

      
    }

}
