package com.careercompass.backend.controller.skill;

import com.careercompass.backend.entity.Skill;
import com.careercompass.backend.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillController {

    @Autowired
    private SkillRepository skillRepository;

    @GetMapping
    public ResponseEntity<Page<Skill>> getAllSkills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Skill> skills = skillRepository.findAll(pageable);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Skill>> getAllSkillsList() {
        List<Skill> skills = skillRepository.findAll();
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) {
        Optional<Skill> skill = skillRepository.findById(id);
        return skill.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Skill> createSkill(@RequestBody Skill skill) {
        Skill savedSkill = skillRepository.save(skill);
        return ResponseEntity.ok(savedSkill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable Long id, @RequestBody Skill skillDetails) {
        Optional<Skill> optionalSkill = skillRepository.findById(id);
        if (optionalSkill.isPresent()) {
            Skill skill = optionalSkill.get();
            skill.setName(skillDetails.getName());
            skill.setCategory(skillDetails.getCategory());
            
            Skill savedSkill = skillRepository.save(skill);
            return ResponseEntity.ok(savedSkill);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        if (skillRepository.existsById(id)) {
            skillRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Skill API is running!");
    }
}