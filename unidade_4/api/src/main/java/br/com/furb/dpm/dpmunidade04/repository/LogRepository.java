package br.com.furb.dpm.dpmunidade04.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.furb.dpm.dpmunidade04.document.LogDocument;

@Repository
public interface LogRepository extends CrudRepository<LogDocument, String> {

}
