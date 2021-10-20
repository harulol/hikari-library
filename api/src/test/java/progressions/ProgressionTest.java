package progressions;

import dev.hawu.plugins.api.math.progressions.ArithmeticProgression;
import dev.hawu.plugins.api.math.progressions.GeometricProgression;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class ProgressionTest {

    @Test
    @DisplayName("Arithmetic Progression test")
    public void arithmeticProgressionTest() {
        final ArithmeticProgression progression = new ArithmeticProgression(1, 5);
        // The progression would be as followed:
        // 1 6 11 16 21 26 31 36 41 46 51 56
        // The 7th term would be 31
        // The 8th term would be the closet to 40 without going over
        Assertions.assertEquals(31, progression.getTerm(7));
        Assertions.assertEquals(8, progression.getNthTerm(40));
    }

    @Test
    @DisplayName("Geometric Progression test")
    public void geometricProgressionTest() {
        final GeometricProgression progression = new GeometricProgression(1, 3);
        // The progression would be as followed:
        // 1 3 9 27 81 243 729 2,187 6,561
        // The 5th term would be 81
        // The 9th term would be the closet to 7,000 without going over
        Assertions.assertEquals(81, progression.getTerm(5));
        Assertions.assertEquals(9, progression.getNthTerm(7000));
    }

}
