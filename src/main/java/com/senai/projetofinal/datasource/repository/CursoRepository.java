package com.senai.projetofinal.datasource.repository;

import com.senai.projetofinal.datasource.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<CursoEntity, Long> {

    boolean existsByNome(String nome);

    @Query("SELECT c FROM CursoEntity c JOIN c.turmas t JOIN t.alunos a WHERE a.id = :idAluno")
    List<CursoEntity> findCursosByAlunoTurma(@Param("idAluno") Long idAluno);
}
