package sia.tacocloud.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sia.tacocloud.data.IngredientRepository;
import sia.tacocloud.model.Ingredient;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Override
    public Ingredient convert(String id) {
        return ingredientRepository.findById(id).get();
    }
}
