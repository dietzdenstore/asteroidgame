package dietz.scoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@RestController
@CrossOrigin   // allow your JavaFX app (localhost) to call it
public class ScoreSystemApplication {

    private int score = 0;

    public static void main(String[] args) {
        SpringApplication.run(dietz.scoring.ScoreSystemApplication.class, args);
    }

    @GetMapping("/score/get")
    public int getScore() {
        return this.score;
    }

    @PutMapping("/score/add/{delta}")
    public int addToScore(@PathVariable int delta) {
        this.score += delta;
        return this.score;
    }

    @PutMapping("/score/set/{value}")
    public int setScore(@PathVariable("value") int value) {
        this.score = value;
        return this.score;
    }
}
