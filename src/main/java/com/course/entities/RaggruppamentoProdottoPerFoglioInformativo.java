package com.course.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Getter
@Setter
@ToString
@Entity
@Table(name = "course")
public class RaggruppamentoProdottoPerFoglioInformativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String raggruppamento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fascia_di_foglio_informativo_per_raggruppamento_prodotto_id")
    private FasciaDiFoglioInformativoPerRaggruppamentoProdotto fasciaDiFoglioInformativoPerRaggruppamentoProdotto;
}