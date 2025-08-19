package com.vnair.usermanagement.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code; // Mã máy bay (ví dụ: VN-A123)

    private String name; // Tên máy bay

    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cabin> cabins;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Cabin> getCabins() { return cabins; }
    public void setCabins(List<Cabin> cabins) { this.cabins = cabins; }
}
