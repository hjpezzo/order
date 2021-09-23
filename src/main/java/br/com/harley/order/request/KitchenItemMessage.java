package br.com.harley.order.request;

import java.util.List;

import br.com.harley.order.entity.KitchenItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KitchenItemMessage {

	private String orderId;
	private List<KitchenItem> kitchenItens;
	
}
