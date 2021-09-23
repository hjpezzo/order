package br.com.harley.order.request;

import java.util.List;

import br.com.harley.order.entity.BarItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BarItemMessage {

	private String orderId;
	private List<BarItem> barItens;
	
}
