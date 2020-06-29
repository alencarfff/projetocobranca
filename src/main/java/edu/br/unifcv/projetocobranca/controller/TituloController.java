package edu.br.unifcv.projetocobranca.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.br.unifcv.projetocobranca.model.Titulo;
import edu.br.unifcv.projetocobranca.repository.Titulos;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/titulos")
public class TituloController {
	private static final String CADASTRO_VIEW = "CadastroTitulo";

	@Autowired
	private Titulos titulos;

	@GetMapping(value = "/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Titulo());
		return mv;
	}

	@PostMapping
	public ModelAndView salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		if(errors.hasErrors()) {
			return mv;
		}

		titulos.save(titulo);
		ModelAndView mv2 = new ModelAndView("redirect:/titulos/novo");
		attributes.addFlashAttribute("mensagem", "Titulo Salvo com Sucesso!");
		return mv2;
	}

	@GetMapping
	public ModelAndView pesquisar() {
		List<Titulo> todosTitulos = titulos.findAll();
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("titulos", todosTitulos);
		return mv;
	}

	@GetMapping(value = "/{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") Long id){
		try {
			Optional<Titulo> titulo = this.titulos.findById(id);

			if( titulo.isPresent() ){
				this.titulos.delete(titulo.get());
			} else {
				throw new EntityNotFoundException();
			}

			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Título deletado com sucesso");
		}
		catch(EntityNotFoundException err){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Título não encontrado.");
		}
	}
}