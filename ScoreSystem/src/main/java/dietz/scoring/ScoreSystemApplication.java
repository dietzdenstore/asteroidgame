package dietz.scoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@RestController
@CrossOrigin  // allow your JavaFX app to call it
public class ScoreSystemApplication {

    // in-memory score
    private int score = 0;

    public static void main(String[] args) {
        SpringApplication.run(ScoreSystemApplication.class, args);
    }

    /** GET current score */
    @GetMapping("/score/get")
    public int getScore() {
        return this.score;
    }

    /** RESET score to a specific value */
    @PutMapping("/score/set/{value}")
    public int setScore(@PathVariable("value") int value) {
        this.score = value;
        return this.score;
    }

    /** ADD delta to the score */
    @PutMapping("/score/add/{delta}")
    public int addToScore(@PathVariable("delta") int delta) {
        this.score += delta;
        return this.score;
    }
}
