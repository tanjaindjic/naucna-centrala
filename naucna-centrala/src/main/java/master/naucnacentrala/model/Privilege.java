/*
 * package master.naucnacentrala.model;
 * 
 * import javax.persistence.*; import java.util.Collection;
 * 
 * //moguce privilegije
 * 
 * @Entity public class Privilege {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
 * 
 * private String name;
 * 
 * @ManyToMany(mappedBy = "privileges") private Collection<Role> roles; }
 */