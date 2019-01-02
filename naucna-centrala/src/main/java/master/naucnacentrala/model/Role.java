/*
 * package master.naucnacentrala.model;
 * 
 * import javax.persistence.*; import java.util.Collection;
 * 
 * //moguce role autor, recenzent i urednik
 * 
 * @Entity public class Role {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
 * 
 * private String name;
 * 
 * @ManyToMany(mappedBy = "roles") private Collection<Korisnik> users;
 * 
 * @ManyToMany
 * 
 * @JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name =
 * "role_id", referencedColumnName = "id"), inverseJoinColumns
 * = @JoinColumn(name = "privilege_id", referencedColumnName = "id")) private
 * Collection<Privilege> privileges; }
 */