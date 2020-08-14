package com.multiple_language_menu.services;

import com.multiple_language_menu.constants.RoleConstant;
import com.multiple_language_menu.models.entities.Logs;
import com.multiple_language_menu.models.entities.Shops;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.responses.dataResponse.ResLog;
import com.multiple_language_menu.repositories.ILogRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import com.multiple_language_menu.repositories.IUserRepository;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {
    @Autowired
    ILogRepository logRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IShopRepository shopRepository;


    //Todo: pagination
    public List<ResLog> getLogs(HttpServletRequest request)
    {
        List<ResLog> resLogs = new ArrayList<ResLog>();
        String token = request.getHeader("Authorization");
        String username = AttributeTokenService.getUsernameFromToken(token);
        Users user = userRepository.findByUsername(username);
        if(AttributeTokenService.checkAccess(token, RoleConstant.ROOT))
        {
            //GetAll log
            List<Logs> logs = logRepository.findAllByOrderByCreatedAtDesc();
            for(Logs log : logs)
            {
                ResLog resLog = new ResLog(log);
                resLogs.add(resLog);
            }
        }
        else if(AttributeTokenService.checkAccess(token,RoleConstant.ADMIN))
        {
            //GetAll log by Shop created_by
            List<Shops> shops = shopRepository.findByCreatedBy(user.getId());
            for (Shops shop : shops)
            {
                List<Logs> logs = logRepository.findAllByShopOrderByCreatedAtDesc(shop);
                for(Logs log : logs)
                {
                    ResLog resLog = new ResLog(log);
                    resLogs.add(resLog);
                }
            }
        }
        else if(AttributeTokenService.checkAccess(token,RoleConstant.MANAGER))
        {
            //Getall log by shop Owner
            Shops shop = shopRepository.findByOwner(user);
            List<Logs> logs = logRepository.findAllByShopOrderByCreatedAtDesc(shop);
            for(Logs log : logs)
            {
                ResLog resLog = new ResLog(log);
                resLogs.add(resLog);
            }

        }
        return resLogs;
    }
}
