package de.patternframeworks.busash.offer

import de.patternframeworks.busash.user.persistance.User
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "offers")
data class Offer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "offer_id") val offerId: Long,
    @Column(name = "title") val title: String,
    @Column(name = "description") val description: String? = "",
    @Enumerated(EnumType.STRING) @Column(name = "category") val category: OfferCategory,
    @Column(name = "quantity") val quantity: String? = "",
    @Enumerated(EnumType.STRING) @Column(name = "price_type") val priceType: PriceType,
    @Column(name = "price") val price: String,
    @Length(max = 15000)
    @Column(name ="product_pic", length = 15000)
    val productPic: String? = "",
    //Information is extracted from authroization header, so we have to allow it being absent from request header
    @ManyToOne @JoinColumn(name = "author_id") var author: User? = null,
    @ManyToOne @JoinColumn(name = "reserved_by_id") var reservedBy: User? = null,
    @Column(name="reservation_time") var reservationTime: LocalDateTime? = null
)
