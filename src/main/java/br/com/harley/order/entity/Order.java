package br.com.harley.order.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.harley.order.enums.Status;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order implements Serializable {

	@Id
	private String id;
	
	private String waiter;
	
	@JsonProperty("table")
	private Integer tableNumber;

	@OneToMany(mappedBy = "order", cascade={CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<KitchenItem> kitchenItens;
	
	@OneToMany(mappedBy = "order", cascade={CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<BarItem> barItens;
	
	@Enumerated(EnumType.STRING)
	private Status statusBar;
	
	@Enumerated(EnumType.STRING)
	private Status statusKitchen;

}
