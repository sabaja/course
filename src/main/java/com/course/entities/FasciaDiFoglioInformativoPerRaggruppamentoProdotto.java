package com.course.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@ToString
@Entity
@Table(name = "course")
public class FasciaDiFoglioInformativoPerRaggruppamentoProdotto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "fasciaDiFoglioInformativoPerRaggruppamentoProdotto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RaggruppamentoProdottoPerFoglioInformativo> elencoRaggruppamentoProdottoPerFoglioInformativo;

    private Long ordine;

    @OneToMany(mappedBy = "fasciaDiFoglioInformativoPerRaggruppamentoProdotto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FasciaDiFoglioInformativo> elencoFasciaDiFoglioInformativo;
}