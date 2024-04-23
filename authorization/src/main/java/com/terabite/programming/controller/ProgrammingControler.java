package com.terabite.programming.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.terabite.programming.model.Program;
import com.terabite.programming.model.RepCycle;
import com.google.api.client.util.Lists;
import com.terabite.programming.dto.CreateDayRequest;
import com.terabite.programming.dto.CreateProgramRequest;
import com.terabite.programming.dto.CreateRepCycleRequest;
import com.terabite.programming.dto.CreateWeekRequest;
import com.terabite.programming.dto.UpdateDayRequest;
import com.terabite.programming.dto.UpdateProgramRequest;
import com.terabite.programming.dto.UpdateRepCycleRequest;
import com.terabite.programming.dto.UpdateWeekRequest;
import com.terabite.programming.model.Day;
import com.terabite.programming.model.Week;
import com.terabite.programming.service.DayService;
import com.terabite.programming.service.ProgramService;
import com.terabite.programming.service.RepCycleService;
import com.terabite.programming.service.WeekService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/v1/programming")
public class ProgrammingControler {
    
    private final RepCycleService repCycleService;
    private final DayService dayService;
    private final WeekService weekService;
    private final ProgramService programService;

    public ProgrammingControler(RepCycleService repCycleService, DayService dayService, WeekService weekService, ProgramService programService){
        this.repCycleService=repCycleService;
        this.dayService=dayService;
        this.weekService=weekService;
        this.programService=programService;
    }

    
    //Program endpoints
    @PostMapping("/program")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> postProgram(@RequestBody CreateProgramRequest request) {
        Program program = new Program(request.programName(), Lists.newArrayList());
        return programService.createNewProgram(program);
    }

    @PutMapping("/program")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateProgram(@RequestBody UpdateProgramRequest program) {
        List<Week> weeks = Lists.newArrayList();
        for (int weekId : program.weekIds()) {
            Week week = new Week();
            week.setWeekId(weekId);
            weeks.add(week);
        }

        return programService.updateProgram(program, weeks);
    }

    @GetMapping("/program/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN', 'ROLE_BASE_CLIENT', 'ROLE_SPECIFIC_CLIENT')")
    public ResponseEntity<?> getProgram(@RequestParam int id) {
        Program program = new Program("name", Lists.newArrayList());
        program.setProgramId(id);

        return programService.getProgram(program);
    }

    @GetMapping("/all_programs")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> getAllPrograms() {
        return programService.getAllPrograms();
    }

    @DeleteMapping("/program/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> deleteProgram(@RequestParam int id){
        Program program = new Program("name", Lists.newArrayList());
        program.setProgramId(id);   

        return programService.deleteProgram(program);
    }

    /**
     * POST MAPING EXAMPLE: 
     <p>
     {
        "weekName": "Week 1",
        "weekDescription": "This is the first week of the program"
     }
     <p>
     */
    @PostMapping("/week")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> createWeek(@RequestBody CreateWeekRequest request) {
        Week week = new Week(request.weekName(), Lists.newArrayList());

        return weekService.createNewWeek(week);
    }

    /**
     * POST MAPING EXAMPLE:
        <p>
        {
            "weekId": 0,
            "weekName": "Week 1",
            "days":[]
        }
        <p>
    */
    @PutMapping("/week")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateWeek(@RequestBody UpdateWeekRequest request) {
        
        List<Day> days = Lists.newArrayList();
        for (int dayId : request.dayIds()) {
            Day day = new Day();
            day.setDayId(dayId);
            days.add(day);
        }
        return weekService.updateWeek(request, days);
    }

    @GetMapping("/week/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN', 'ROLE_BASE_CLIENT', 'ROLE_SPECIFIC_CLIENT')")
    public ResponseEntity<?> getWeek(@RequestParam long id) {
        Week week = new Week("name", Lists.newArrayList());
        week.setWeekId(id);
        return weekService.getWeek(week);
    }

    @GetMapping("/week/all_weeks")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> getAllWeeks() {
        return weekService.getAllWeeks();
    }
    
    @DeleteMapping("/week/{id}")
    public ResponseEntity<?> deleteWeek(@RequestParam long id){
        Week week = weekService.getWeekById(id);

        return weekService.deleteWeekByName(week);
    }


    //Day endpoints
    @PostMapping("/day")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> createDay(@RequestBody CreateDayRequest request) {
        Day day = new Day(request.dayName(), Lists.newArrayList());

        return dayService.createNewDay(day);
    }

    @PutMapping("/day")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateDay(@RequestBody UpdateDayRequest request) {

        List<RepCycle> repCycles = Lists.newArrayList();
        for (int repCycleId : request.repCycleIds()) {
            RepCycle repCycle = new RepCycle();
            repCycle.setRepCycleId(repCycleId);
            repCycles.add(repCycle);
        }
        return dayService.updateDay(request, repCycles);
    }

    @GetMapping("/day/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN', 'ROLE_BASE_CLIENT', 'ROLE_SPECIFIC_CLIENT')")
    public ResponseEntity<?> getDay(@RequestParam long id) {
        Day day = new Day("name", Lists.newArrayList());
        return dayService.getDay(day);
    }

    @GetMapping("/day/all_days")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> getAllDays() {
        return dayService.getAllDays();
    }

    @DeleteMapping("/day/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> deleteDay(@RequestParam long id){
        Day day = new Day("name", Lists.newArrayList());
        return dayService.deleteDay(day);
    }


    //RepCycle endpoints
    @PostMapping("/rep_cycle")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> postRepCycle(@RequestBody CreateRepCycleRequest request) {
        RepCycle repCycle = new RepCycle().withName(request.repCycleName())
                                        // .withDescription(request.repCycleDescription())
                                        .withNumReps(request.numReps())
                                        .withNumSets(request.numSets())
                                        .withWeight(request.weight())
                                        .withRestTime(request.restTime())
                                        .withCoachNotes(request.coachNotes())
                                        .withWorkoutOrder(request.workoutOrder())
                                        .withEquipment(request.equipment())
                                        .withMovementId(request.movementId());

        return repCycleService.createNewRepCycle(repCycle);
    }

    @PutMapping("/rep_cycle")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> putRepCycle(@RequestBody UpdateRepCycleRequest repCycle) {
        RepCycle updatedRepCycle = new RepCycle()
                                        .withRepCycleId(repCycle.id())
                                        .withName(repCycle.repCycleName().orElse(null))
                                        .withEquipment(repCycle.equipment().orElse(null))
                                        .withNumSets(repCycle.numSets().orElse(null))
                                        .withNumReps(repCycle.numReps().orElse(null))
                                        .withWeight(repCycle.weight().orElse(null))
                                        .withRestTime(repCycle.restTime().orElse(null))
                                        .withCoachNotes(repCycle.coachNotes().orElse(null))
                                        .withWorkoutOrder(repCycle.workoutOrder().orElse(null))
                                        .withMovementId(repCycle.movementId().orElse(null));

        return repCycleService.updateRepCycle(updatedRepCycle);
    }

    @GetMapping("/rep_cycle/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN', 'ROLE_BASE_CLIENT', 'ROLE_SPECIFIC_CLIENT')")
    public ResponseEntity<?> getRepCycle(@RequestParam long id) {
        RepCycle repCycle = new RepCycle().withRepCycleId(id);
        return repCycleService.getRepCycle(repCycle);
    }

    @GetMapping("/rep_cycle/all_rep_cycles")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> getAllRepCycles() {
        return repCycleService.getAllRepCycles();
    }

    @DeleteMapping("/rep_cycle/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_COACH', 'ROLE_ADMIN')")
    public ResponseEntity<?> deleteRepCycle(@RequestParam long id){
        RepCycle repCycle = new RepCycle().withRepCycleId(id);
        return repCycleService.deleteRepCycle(repCycle);
    }

}
