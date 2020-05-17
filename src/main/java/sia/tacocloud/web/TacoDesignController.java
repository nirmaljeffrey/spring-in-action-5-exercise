package sia.tacocloud.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import sia.tacocloud.model.Ingredient;
import sia.tacocloud.model.Ingredient.Type;
import sia.tacocloud.data.IngredientRepository;
import sia.tacocloud.data.TacoRepository;
import sia.tacocloud.model.Order;
import sia.tacocloud.model.Taco;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class TacoDesignController {
@ModelAttribute(name = "order")
public Order order(){
    return new Order();
}

    @ModelAttribute(name = "taco")
    public Taco taco(){
        return new Taco();
    }
    private IngredientRepository ingredientRepository;
    private TacoRepository tacoRepository;

    @Autowired
    public TacoDesignController(IngredientRepository ingredientRepository, TacoRepository tacoRepository){
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
    }

    @GetMapping
    public String showDesignForm(Model model){
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientRepository.findAll().forEach(ingredientList::add);
        Type[] types = Ingredient.Type.values();
        for(Type type : types){
            model.addAttribute(type.toString().toLowerCase(),filterByType(ingredientList,type));
        }
        return "design";
    }


    private List<Ingredient> filterByType(List<Ingredient> ingredients,Type type){
        return ingredients.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
    }

    @PostMapping
    public String processDesign(@Valid Taco taco, Errors errors,@ModelAttribute Order order){
        if(errors.hasErrors()){
            return "design";
        }
        Taco savedTaco = tacoRepository.save(taco);
        order.addDesign(savedTaco);
        log.info("Processing design: "+ taco);
        return "redirect:orders/current";
    }
}

