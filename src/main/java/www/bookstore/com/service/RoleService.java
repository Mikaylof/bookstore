package www.bookstore.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import www.bookstore.com.dto.request.ReqRole;
import www.bookstore.com.dto.response.RespStatus;
import www.bookstore.com.dto.response.RespStatusList;
import www.bookstore.com.entity.Role;
import www.bookstore.com.exception.ExceptionConstants;
import www.bookstore.com.exception.MyException;
import www.bookstore.com.repository.RoleRepository;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RespStatusList addRole(ReqRole reqRole) {
        RespStatusList respStatusList = new RespStatusList();
        try {
            String roleName = reqRole.getRole();
            if (roleName == null) {
                throw new MyException(ExceptionConstants.INVALID_REQUEST, "request fail");
            }
            Optional<Role> role =roleRepository.findRoleByRole(roleName);
            if (role.isPresent()){
                throw new MyException(ExceptionConstants.DATA_TAKEN,"data taken");
            }
            Role r=new Role();
            r.setRole(reqRole.getRole());
            roleRepository.save(r);
            respStatusList.setStatus(RespStatus.getMessage());
        } catch (MyException e) {
             e.printStackTrace();
             respStatusList.setStatus(new RespStatus(e.getCode(),e.getMessage()));
        }catch (Exception e){
            e.printStackTrace();
            respStatusList.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION,"Internal error"));
        }
        return respStatusList;
    }
}
