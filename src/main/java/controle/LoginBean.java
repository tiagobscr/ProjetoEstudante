package controle;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

import dao.AlunoDao;
import dao.AlunoDaoImpl;
import entidade.Aluno;
import util.CalculoIdade;

/**
 * 
 * @author Tiago Batista
 *
 *         LoginBean, classe responsavel pela logica de logar no sistema
 *
 */

@ManagedBean(name = "LoginBean")
@SessionScoped
public class LoginBean {

	private String usuarioTXT;
	private String senhaTXT;
	AlunoDao alunoDao;

	Aluno aluno;
	private String mensagem;
	EntityManagerFactory factory;
	private Aluno usuarioLogado;
	private LocalDate dataNascimento;
	private String data;
	private String confirmaSenha;

	private static final String PESQUISAR = "pesquisarUsuario.xhtml";

	private static final String Manter = "manterUsuario.xhtml";

	private static final String Login = "index.xhtml";

	private static final String Senha = "esqueciSenha.xhtml";

	private static final String MATERIA = "pesquisarMaterias.xhtml";

	public LoginBean() {
		this.aluno = new Aluno();
		this.alunoDao = new AlunoDaoImpl();

	}

	public void entrar() throws IOException {

		if (this.usuarioTXT.equals("admin") && this.senhaTXT.equals("admin")) {
			this.usuarioLogado = new Aluno();
			this.usuarioLogado.setNome("admin");
			HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			sessao.setAttribute("usuarioLogado", usuarioLogado);
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(PESQUISAR + "?faces-redirect=true&amp;includeViewParams=true");
		} else {
			if (this.alunoDao.pesquisar(this.usuarioTXT) != null) {
				if (this.alunoDao.pesquisar(this.usuarioTXT).getSenha().equals(this.senhaTXT)) {
					this.usuarioLogado = alunoDao.pesquisar(this.usuarioTXT);
					this.aluno = this.usuarioLogado;
					HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
							.getSession(true);
					sessao.setAttribute("usuarioLogado", usuarioLogado);

					// "/paginas/feira/manterFeira.xhtml?faces-redirect=true&amp;includeViewParams=true";

					FacesContext.getCurrentInstance().getExternalContext()
							.redirect(MATERIA + "?faces-redirect=true&amp;includeViewParams=true");
				} else {
					this.mensagem = " Senha inválida";
				}
			} else {

				this.mensagem = " Usuario não cadastrado";
			}

		}
	}

	public boolean habilitaBotao() {
		return this.aluno.getEmail().equals(this.usuarioLogado.getEmail());
	}

	public void abrirManterUsuario() throws IOException {
		this.usuarioLogado = null;
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		sessao.setAttribute("usuarioLogado", usuarioLogado);
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect(Manter + "?faces-redirect=true&amp;includeViewParams=true");
	}

	public void abrirEsqueciSenha() throws IOException {

		if (this.alunoDao.pesquisar(this.usuarioTXT) != null) {
			this.aluno = new Aluno();
			this.aluno.setEmail(this.usuarioTXT);

		} else {
			this.aluno.setEmail("");

		}

		FacesContext.getCurrentInstance().getExternalContext().redirect(Senha);
	}

	public void novaSenha() throws IOException {

		if (this.alunoDao.pesquisar(this.aluno.getEmail()) != null) {

			Aluno alunoAtual = this.alunoDao.pesquisar(this.aluno.getEmail());
			System.out.println(alunoAtual.getNome());
			System.out.println(alunoAtual.getDataNascimento());
			System.out.println(this.aluno.getNome());
			System.out.println(this.aluno.getDataNascimento());
			if (this.aluno.getNome().equals(alunoAtual.getNome())
					&& this.aluno.getDataNascimento().equals(alunoAtual.getDataNascimento())) {
				alunoAtual.setSenha(this.aluno.getSenha());
				alunoDao.alterar(alunoAtual);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						alunoAtual.getNome() + " senha alterada com sucesso."));
				this.mensagem = " Senha alterada com sucesso.";
				alunoAtual = new Aluno();
				System.out.println("alterado");
				abrirLogin();
			} else {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " dados inválidos."));
				alunoAtual = new Aluno();
				this.aluno = new Aluno();

			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " Usuário inválido."));

			this.mensagem = " Usuário inválido.";

		}

	}

	public void convertDate() throws ParseException {

		System.out.println(data);
		dataNascimento = CalculoIdade.format(this.data);
		this.aluno.setIdade(CalculoIdade.idade(dataNascimento));
		System.out.println(this.aluno.getIdade());
		// this.usuario.setDataNascimento(dataNascimento);
		this.aluno.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(data));

	}

	public void abrirLogin() throws IOException {
		this.usuarioLogado = null;
		FacesContext.getCurrentInstance().getExternalContext().redirect(Login);

	}

	public String getUsuarioTXT() {
		return usuarioTXT;
	}

	public void setUsuarioTXT(String usuarioTXT) {
		this.usuarioTXT = usuarioTXT;
	}

	public String getSenhaTXT() {
		return senhaTXT;
	}

	public void setSenhaTXT(String senhaTXT) {
		this.senhaTXT = senhaTXT;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Aluno getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Aluno usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

}
