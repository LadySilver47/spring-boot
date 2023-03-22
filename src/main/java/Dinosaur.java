import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Dinosaur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean fangs;

    private int numberOfArms;

    private double weightTons;

    @Override
    public String toString() {
        return "Dinosaur{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fangs=" + fangs +
                ", numberOfArms=" + numberOfArms +
                ", weightTons=" + weightTons +
                '}';
    }
}
