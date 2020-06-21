package br.com.furb.dpm.dpmunidade04.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.furb.dpm.dpmunidade04.document.DeviceDocument;

@Repository
public interface DeviceRepository extends CrudRepository<DeviceDocument, String> {

}
