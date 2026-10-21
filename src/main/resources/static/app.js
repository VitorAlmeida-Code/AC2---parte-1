const API_PROJETOS = "/api/projetos";

const form = document.getElementById("form-projeto");
const btnSalvar = document.getElementById("btn-salvar");
const btnAtualizar = document.getElementById("btn-atualizar");
const mensagem = document.getElementById("mensagem");
const listaProjetos = document.getElementById("lista-projetos");

function formatarData(data) {
    if (!data) return "-";
    const [ano, mes, dia] = data.split("-");
    return `${dia}/${mes}/${ano}`;
}

function mostrarSucesso(texto) {
    mensagem.className = "mensagem sucesso";
    mensagem.textContent = texto;
    mensagem.hidden = false;
}

function mostrarErros(erros) {
    mensagem.className = "mensagem erro";
    mensagem.innerHTML = "";
    const ul = document.createElement("ul");
    erros.forEach((erro) => {
        const li = document.createElement("li");
        li.textContent = erro;
        ul.appendChild(li);
    });
    mensagem.appendChild(ul);
    mensagem.hidden = false;
}

async function lerErros(resposta) {
    try {
        const corpo = await resposta.json();
        if (corpo && Array.isArray(corpo.errors) && corpo.errors.length > 0) {
            return corpo.errors;
        }
    } catch (e) {
    }
    return [`Erro inesperado (HTTP ${resposta.status}).`];
}

function linhaMensagem(texto) {
    listaProjetos.innerHTML = "";
    const tr = document.createElement("tr");
    const td = document.createElement("td");
    td.colSpan = 5;
    td.className = "vazio";
    td.textContent = texto;
    tr.appendChild(td);
    listaProjetos.appendChild(tr);
}

function renderizarProjetos(projetos) {
    if (projetos.length === 0) {
        linhaMensagem("Nenhum projeto cadastrado.");
        return;
    }
    listaProjetos.innerHTML = "";
    projetos.forEach((p) => {
        const tr = document.createElement("tr");
        const nomes = (p.funcionarios || []).map((f) => f.nome).join(", ") || "-";
        [p.id, p.descricao, formatarData(p.dataInicio), formatarData(p.dataFim), nomes].forEach((valor) => {
            const td = document.createElement("td");
            td.textContent = valor;
            tr.appendChild(td);
        });
        listaProjetos.appendChild(tr);
    });
}

async function carregarProjetos() {
    try {
        const resposta = await fetch(API_PROJETOS);
        if (!resposta.ok) {
            mostrarErros(await lerErros(resposta));
            linhaMensagem("Não foi possível carregar os projetos.");
            return;
        }
        renderizarProjetos(await resposta.json());
    } catch (e) {
        mostrarErros(["Não foi possível conectar ao servidor."]);
        linhaMensagem("Não foi possível carregar os projetos.");
    }
}

async function salvarProjeto(evento) {
    evento.preventDefault();

    const projeto = {
        descricao: form.descricao.value,
        dataInicio: form.dataInicio.value || null,
        dataFim: form.dataFim.value || null,
    };

    btnSalvar.disabled = true;
    try {
        const resposta = await fetch(API_PROJETOS, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(projeto),
        });

        if (!resposta.ok) {
            mostrarErros(await lerErros(resposta));
            return;
        }

        form.reset();
        mostrarSucesso("Projeto cadastrado com sucesso!");
        await carregarProjetos();
    } catch (e) {
        mostrarErros(["Não foi possível conectar ao servidor."]);
    } finally {
        btnSalvar.disabled = false;
    }
}

form.addEventListener("submit", salvarProjeto);
btnAtualizar.addEventListener("click", carregarProjetos);

carregarProjetos();
