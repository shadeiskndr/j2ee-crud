package com.example.service;

import com.example.dao.StateDAO;
import com.example.dao.StateDAOImpl;
import com.example.model.State;

import java.util.ArrayList;
import java.util.List;

public class StateServiceImpl implements StateService {
    private final StateDAO stateDAO;

    public StateServiceImpl() {
        this.stateDAO = new StateDAOImpl();
    }

    @Override
    public List<State> getAllStates() {
        try {
            return stateDAO.getAllStates();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
