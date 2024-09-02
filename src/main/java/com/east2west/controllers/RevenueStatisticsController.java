package com.east2west.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.east2west.models.DTO.RentalRevenueDTO;
import com.east2west.models.DTO.TourRevenueDTO;
import org.springframework.web.bind.annotation.RequestParam;
import com.east2west.service.Analysis;
import java.util.List;
@RestController
@RequestMapping("/api/revenuestatistics")
public class RevenueStatisticsController {

    @Autowired
    private Analysis revenueStatisticsService;

    @GetMapping("/toptours/month")
    public List<TourRevenueDTO> getTopBookedToursByMonth(@RequestParam int year, @RequestParam int month) {
        return revenueStatisticsService.getTopBookedToursByMonth(year, month);
    }

    @GetMapping("/toptours/week")
    public List<TourRevenueDTO> getTopBookedToursByWeek(@RequestParam int year, @RequestParam int week) {
        return revenueStatisticsService.getTopBookedToursByWeek(year, week);
    }

    @GetMapping("/toptours/year")
    public List<TourRevenueDTO> getTopBookedToursByYear(@RequestParam int year) {
        return revenueStatisticsService.getTopBookedToursByYear(year);
    }

    @GetMapping("/topcars/month")
    public List<RentalRevenueDTO> getTopRentedCarsByMonth(@RequestParam int year, @RequestParam int month) {
        return revenueStatisticsService.getTopRentedCarsByMonth(year, month);
    }

    @GetMapping("/topcars/week")
    public List<RentalRevenueDTO> getTopRentedCarsByWeek(@RequestParam int year, @RequestParam int week) {
        return revenueStatisticsService.getTopRentedCarsByWeek(year, week);
    }

    @GetMapping("/topcars/year")
    public List<RentalRevenueDTO> getTopRentedCarsByYear(@RequestParam int year) {
        return revenueStatisticsService.getTopRentedCarsByYear(year);
    }
}