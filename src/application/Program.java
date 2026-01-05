package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import db.DB;
import db.DbIntegrityException;

public class Program {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	static Scanner sc = new Scanner(System.in);
	static Connection conn = null;
	static Statement st = null;
	static PreparedStatement ps = null;
	static ResultSet rs = null;
	
	public static void main(String[] args) {
		int opcao;
			
		do {
			System.out.println();
			menu();
			opcao = sc.nextInt();
			
			switch(opcao) {
				case 1->{
					cadastrar();
					break;
				}
							
				case 2->{
					listar();
					break;
				}
				
				case 3->{
					buscarId();
					break;
				}
					
				case 4->{
					buscarNome();
					break;
				}
					
				case 5->{
					atualizar();	
					break;
				}
					
				case 6->{
					excluir();	
					break;
				}
					
				case 0->{
					fecharConexao();
					System.out.println("Encerrando Programa...");
					break;
				}
			}
				
				
				
			}while(opcao!=0);
			
		
	}

	private static void menu() {
		System.out.println("1-Cadastrar Cliente.");
		System.out.println("2-Listar Clientes.");
		System.out.println("3-Buscar Cliente Por Id.");
		System.out.println("4-Buscar Cliente Por Nome.");
		System.out.println("5-Atualizar Dados do Cliente.");
		System.out.println("6-Excluir Cliente.");
		System.out.println("0-Sair.");
	}
	
	private static void cadastrar() {
		try {
			conn = DB.getConnection();
			sc.nextLine();
			System.out.print("Nome: ");
			String nome = sc.nextLine();
			System.out.print("Data de nascimento: ");
			String dt = sc.nextLine();
			
			java.util.Date data;
			
			try {
				data = sdf.parse(dt);
			} catch (ParseException e) {
				e.printStackTrace();
				return;
			}
			
			System.out.print("Telefone: ");
			String tel = sc.nextLine();
			System.out.print("Email: ");
			String email = sc.nextLine();
			
			ps = conn.prepareStatement("insert into cliente (nome, data_nasc, telefone, email) values (?, ?, ?, ?)");
			
			java.sql.Date dt2 = new java.sql.Date(data.getTime());
			ps.setString(1,nome);
			ps.setDate(2, dt2);
			ps.setString(3, tel);
			ps.setString(4, email);
				
			ps.executeUpdate();
			System.out.println("Operação Realizada com Sucesso!");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void listar() {
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery("select * from cliente");
			
			while(rs.next()) {
				System.out.println(rs.getInt("id") + "| " + rs.getString("nome") + "| " + rs.getDate("data_nasc") + 
				"| " + rs.getString("telefone") + "| " + rs.getString("email"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void buscarId() {
		try {
			conn = DB.getConnection();
			System.out.print("Id: ");
			int id = sc.nextInt();
			
			ps = conn.prepareStatement("select * from cliente where id = ?");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getInt("id") + "| " + rs.getString("nome") + "| " + rs.getDate("data_nasc") + 
						"| " + rs.getString("telefone") + "| " + rs.getString("email"));
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void buscarNome() {
		try {
			conn = DB.getConnection();
			sc.nextLine();
			System.out.print("Nome:");
			String name = sc.nextLine();
			ps = conn.prepareStatement("select * from cliente where nome = ?");
			
			ps.setString(1, name);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getInt("id") + "| " + rs.getString("nome") + "| " + rs.getDate("data_nasc") + 
						"| " + rs.getString("telefone") + "| " + rs.getString("email"));
			}	
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void atualizar() {
		try {
			conn = DB.getConnection();
			System.out.print("Atualizar qual atributo? (n - nome, d - data_nasc, t - telefone, e - email): ");
			char op = sc.next().charAt(0);
			int qtdL;
			
			if(op == 'n') {
				System.out.print("Qual o id? ");
				int id = sc.nextInt();
				System.out.print("Nome: ");
				String nome = sc.nextLine();
				ps = conn.prepareStatement("update cliente set nome = ? where id = ?");
				
				ps.setString(1, nome);
				ps.setInt(2, id);
				
				qtdL = ps.executeUpdate();
				
				if(qtdL > 0) System.out.println("Operação Realizada com Sucesso!");
				else System.out.println("Não há clientes.");
				
			}else if(op == 'd') {
				System.out.print("Qual o id? ");
				int id = sc.nextInt();
				sc.nextLine();
				System.out.print("Data de nascimento: ");
				String dt = sc.nextLine();
				
				java.util.Date data;
				
				try {
					data = sdf.parse(dt);
				} catch (ParseException e) {
					e.printStackTrace();
					return;
				}
				
				java.sql.Date dt2 = new java.sql.Date(data.getTime());
				
				ps = conn.prepareStatement("update cliente set data_nasc = ? where id = ?");
				
				ps.setDate(1, dt2);
				ps.setInt(2, id);
				
				qtdL = ps.executeUpdate();
				
				if(qtdL > 0) System.out.println("Operação Realizada com Sucesso!");
				else System.out.println("Não há clientes.");
				
			}else if(op == 't') {
				System.out.print("Qual o id? ");
				int id = sc.nextInt();
				System.out.print("Telefone: ");
				String tel = sc.nextLine();
				
				ps = conn.prepareStatement("update cliente set telefone = ? where id = ?");
				
				ps.setString(1, tel);
				ps.setInt(2, id);
				
				qtdL = ps.executeUpdate();
				
				if(qtdL > 0) System.out.println("Operação Realizada com Sucesso!");
				else System.out.println("Não há clientes.");
				
			}else if(op == 'e') {
				System.out.print("Qual o id? ");
				int id = sc.nextInt();
				System.out.print("Email: ");
				String email = sc.nextLine();
				
				ps = conn.prepareStatement("update cliente set email = ? where id = ?");
				
				ps.setString(1, email);
				ps.setInt(2, id);
				
				qtdL = ps.executeUpdate();
				
				if(qtdL > 0) System.out.println("Operação Realizada com Sucesso!");
				else System.out.println("Não há clientes.");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void excluir() {
		try {
			conn = DB.getConnection();
			
			System.out.print("Id do cliente: ");
			int id = sc.nextInt();
			
			ps = conn.prepareStatement("delete from cliente where id = ?");
			
			ps.setInt(1, id);
			
			int qtdL = ps.executeUpdate();
			
			if(qtdL > 0) System.out.println("Operação Realizada com Sucesso!");
			else System.out.println("Não há clientes.");
			
			
		}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}
			
	}
	
	private static void fecharConexao() {
		DB.closeStatement(st);
		DB.closeStatement(ps);
		DB.closeResultSet(rs);
		DB.closeConnection();
	}
}
