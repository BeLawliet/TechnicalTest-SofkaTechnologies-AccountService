package com.app.service;

import com.app.presentation.dto.AccountDTO;
import com.app.presentation.dto.CustomerEventDTO;
import java.util.List;

public interface IAccountService {
    List<AccountDTO> findAll();
    void save(CustomerEventDTO event);
    void updateAndDelete(CustomerEventDTO event);
}
