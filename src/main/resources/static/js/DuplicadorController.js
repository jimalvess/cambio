class DuplicadorController {
    carregarLista(lista) {
        let corpoTabela = document.getElementById("corpoTabela");
        corpoTabela.innerHTML = "";
        for (let i = 0; i < lista.length; i++) {
            let linha = corpoTabela.insertRow();

            let idCell = linha.insertCell();
            idCell.innerHTML = lista[i].id;

            let nomeCell = linha.insertCell();
            nomeCell.innerHTML = lista[i].nome;

            let celularCell = linha.insertCell();
            celularCell.innerHTML = lista[i].celular;
            
            let emailCell = linha.insertCell();
            emailCell.innerHTML = lista[i].email;
            
            let pixCell = linha.insertCell();
            pixCell.innerHTML = lista[i].pix;
            
            let loginCell = linha.insertCell();
            loginCell.innerHTML = lista[i].login;
            
            let senhaCell = linha.insertCell();
            senhaCell.innerHTML = lista[i].senha;

            let apagarCell = linha.insertCell();
            apagarCell.innerHTML = `<button 
                         onclick="duplicadorController.apagar(${lista[i].id})">Apagar</button>`;

            let editarCell = linha.insertCell();
            editarCell.innerHTML = `<button 
                         onclick="duplicadorController.editarItem(${lista[i].id})">Editar</button>`;
        }
    }

    editarItem(id) {
        fetch(`api/duplicadores/${id}`, {
            method: "GET"
        }).then((resposta) => {
            if (resposta.ok) {
                resposta.json().then(
                        (item) => {
                    this.atribuirItem(item);
                }
                );
            }
        });
    }
    atribuirItem(item) {
        document.getElementById("id").value = item.id;
        document.getElementById("nome").value = item.nome;
        document.getElementById("celular").value = item.valor;
        document.getElementById("email").value = item.valor;
        document.getElementById("pix").value = item.valor;
        document.getElementById("login").value = item.valor;
        document.getElementById("senha").value = item.valor;
    }

    apagar(id) {
        fetch(`api/duplicadores/${id}`, {
            method: "DELETE"
        }
        ).then((resposta) => {
            if (resposta.ok) {
                this.listar();
            } else {
                console.log("Erro ao apagar");
            }

        });

    }

    pesquisar() {
        let pesquisa = document.getElementById("pesquisar").value;
        fetch(`/api/duplicadores/pesquisar/nome?contem=${pesquisa}`, {method: "GET"})
                .then((resultado) => {
                    if (resultado.ok) {
                        // retorno ok
                        resultado.json().then(
                                (lista) => {
                            this.carregarLista(lista);
                            console.log(lista);
                        }
                        );

                    } else {
                        // tratar o erro 
                        console.log("Erro na excecução");


                    }

                }

                );

    }

    listar() {
        fetch("api/duplicadores/", {method: "GET"})
                .then((resultado) => {
                    if (resultado.ok) {
                        // retorno ok
                        resultado.json().then(
                                (lista) => {
                            this.carregarLista(lista);
                            console.log(lista);
                        }
                        );

                    } else {
                        // tratar o erro 
                        console.log("Erro na excecução");


                    }

                }

                );

    }

    confirmar() {
        let id = document.getElementById("id").value;
        let nome = document.getElementById("nome").value;
        let celular = document.getElementById("celular").value;
        let email = document.getElementById("email").value;
        let pix = document.getElementById("pix").value;
        let login = document.getElementById("login").value;
        let senha = document.getElementById("senha").value;

        let item = {
            nome: nome,
            celular: celular,
            email : email,
            pix : pix,
            login : login,
            senha : senha
        };
        if (id == "") {
            this.inserir(item);
        } else {
            this.editar(id, item);
        }
    }

    editar(id, item) {
        fetch(`api/duplicadores/${id}`, {
            method: "PUT",
            headers: new Headers({
                'Content-Type': 'application/json'
            }),
            body: JSON.stringify(item)
        }).then((resultado) => {
            if (resultado.ok) {
                this.limpar();
                this.listar();
            }
        });
    }

    limpar() {
        document.getElementById("id").value = "";
        document.getElementById("nome").value = "";
        document.getElementById("celular").value = "";
        document.getElementById("email").value = "";
        document.getElementById("pix").value = "";
        document.getElementById("login").value = "";
        document.getElementById("senha").value = "";
    }

    inserir(item) {
        fetch("api/duplicadores/", {
            method: "POST",
            headers: new Headers({
                'Content-Type': 'application/json'
            }),
            body: JSON.stringify(item)
        }).then((resultado) => {
            if (resultado.ok) {
                this.listar();
            } else {
                console.log("Erro na execução");
            }

        });

    } 

}