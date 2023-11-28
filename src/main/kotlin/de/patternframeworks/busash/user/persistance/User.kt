package de.patternframeworks.busash.user.persistance

import de.patternframeworks.busash.location.Location
import de.patternframeworks.busash.offer.Offer
import org.hibernate.validator.constraints.Length
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") val id: Long,
        @Column(name = "name") val name: String,
        @Column(name = "email", unique = true) val email: String,
        @Column(name = "password") val password: String,
        @Length(max = 15000)
        @Column(name = "picture", length = 15000) val picture: String? = "",
        @Column(name = "transactions") val transactions: Int? = 0,
        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "location_id")
        var location: Location? = null,
        @OneToMany(mappedBy = "author", fetch = FetchType.EAGER) var offers: List<Offer>? = null
)
