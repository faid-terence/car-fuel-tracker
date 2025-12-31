package com.fueltracker.backend.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fueltracker.backend.model.FuelStats;
import com.fueltracker.backend.service.CarService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FuelStatsServlet extends HttpServlet {

    private CarService carService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        if (context != null) {
            this.carService = context.getBean(CarService.class);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String carIdParam = req.getParameter("carId");
        
        if (carIdParam == null || carIdParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing carId parameter");
            return;
        }

        try {
            Long carId = Long.parseLong(carIdParam);
            FuelStats stats = carService.calculateFuelStats(carId);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(resp.getWriter(), stats);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid carId format");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found or error calculating stats");
        }
    }
}
