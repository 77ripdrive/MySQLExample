package entity;

public class Worker {
    private int id;
    private String firstName;
    private String position;
    private int salary;

    public Worker(int id, String firstName, String position, int salary) {
        this.id = id;
        this.firstName = firstName;
        this.position = position;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPosition() {
        return position;
    }

    public int getSalary() {
        return salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Worker worker = (Worker) o;

        if (id != worker.id) return false;
        if (salary != worker.salary) return false;
        if (!firstName.equals(worker.firstName)) return false;
        return position.equals(worker.position);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + salary;
        return result;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                '}';
    }
}
