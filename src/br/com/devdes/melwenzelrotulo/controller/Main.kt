package br.com.devdes.melwenzelrotulo.controller

import br.com.devdes.melwenzelrotulo.model.Criar
import br.com.devdes.melwenzelrotulo.model.Rotulo
import javafx.concurrent.Service
import javafx.concurrent.Task
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.net.URL
import java.time.OffsetDateTime
import java.util.*
import kotlin.collections.ArrayList

class Main : Initializable{

    @FXML var tfQtdRotulo : TextField? = null
    @FXML var tfLote : TextField? = null
    @FXML var tfFloracao : TextField? = null
    @FXML var tfPesoBruto : TextField? = null
    @FXML var tfPesoLiquido : TextField? = null

    @FXML var cbImagemFundo : CheckBox? = null

    @FXML var dpDataEmpacotamento : DatePicker? = null
    @FXML var dpValidade : DatePicker? = null

    @FXML var pbLoad : ProgressBar? = null

    @FXML var btnLimpaCampo : Button? = null
    @FXML var btnSalvarArquivo : Button? = null

    @FXML var vbFormulario : VBox? = null

    private fun exibeAlertaFormulario() {
        exibeAlerta(
                Alert.AlertType.ERROR,
                "Entrada Incorreta",
                "Porfavor verificar os campos do formulário."
        )
    }

    private fun exibeAlertaFinal() {
        exibeAlerta(
                Alert.AlertType.INFORMATION,
                "Informação",
                "Arquivos Gerados"
        )
    }

    private fun chamaSaveDialog(fileName : String) : File? {
        val fileChooser = FileChooser()
        val extFilter = FileChooser.ExtensionFilter("Arquivos PDF", "*.pdf")

        fileChooser.initialFileName = fileName

        fileChooser.extensionFilters.add(extFilter)

        return fileChooser.showSaveDialog(Stage())
    }

    private fun limparCampos() {
        tfQtdRotulo?.clear()
        tfLote?.clear()
        tfFloracao?.clear()
        tfPesoBruto?.clear()
        tfPesoLiquido?.clear()

        dpDataEmpacotamento?.value = null
        dpValidade?.value = null

        cbImagemFundo?.selectedProperty()?.value = false
    }

    private fun verificarCampos() : Boolean {

        tfQtdRotulo?.text?.toIntOrNull() ?: return false
        tfLote?.text?.toIntOrNull() ?: return false
        tfPesoBruto?.text?.toFloatOrNull() ?: return false
        tfPesoLiquido?.text?.toFloatOrNull() ?: return false
        tfFloracao?.text ?: return false

        dpDataEmpacotamento?.value ?: return false
        dpValidade?.value ?: return false

        return true
    }

    private fun exibeAlerta(alertType: Alert.AlertType, titulo : String, cabecalho : String) {
        val alert = Alert(alertType)
        alert.title = titulo
        alert.headerText = cabecalho

        alert.showAndWait()
    }

    private fun montarRotulos() : ArrayList<Rotulo> {

        val rotulos = ArrayList<Rotulo>()
        val rotulo = Rotulo()

        val quantidade = tfQtdRotulo?.text?.toIntOrNull() ?: return rotulos

        val lote = tfLote?.text?.toIntOrNull() ?: return rotulos
        val floracao = tfFloracao?.text ?: return rotulos
        val pesoBruto = tfPesoBruto?.text?.toFloatOrNull() ?: return rotulos
        val pesoLiquido = tfPesoLiquido?.text?.toFloatOrNull() ?: return rotulos

        val dataEmpacotamento = dpDataEmpacotamento?.value ?: return rotulos

        val validade = dpValidade?.value ?: return rotulos


        val dataEmpacotamentoDate = Date.from(dataEmpacotamento.atStartOfDay().toInstant(OffsetDateTime.now().offset))
        val validadeDate = Date.from(validade.atStartOfDay().toInstant(OffsetDateTime.now().offset))

        rotulo.lote = lote
        rotulo.floracao = floracao
        rotulo.pesoBruto = pesoBruto
        rotulo.pesoLiquido = pesoLiquido

        rotulo.dataEmpacotamento = dataEmpacotamentoDate
        rotulo.validade = validadeDate

        for (i in 1..quantidade) {
            if(i > 8) break // LIMITE DE QUANTIDADE PARA TESTE
            rotulos.add(rotulo)
        }

        return rotulos
    }

    private fun gerarRotulo(nomeArquivo : String, rotulos : ArrayList<Rotulo>) {
        pbLoad?.opacity = 1.0
        btnSalvarArquivo?.isDisable = true
        vbFormulario?.isDisable = true

        val service = object : Service<Void>() {
            override
            fun createTask(): Task<Void> = object : Task<Void>() {
                override
                fun call(): Void? {
                    val criar = Criar()
                    var comImagemFundo = cbImagemFundo?.selectedProperty()?.value

                    if(comImagemFundo == null) comImagemFundo = false

                    criar.pdfRotulos(rotulos, nomeArquivo, comImagemFundo)

                    return null
                }
            }
        }

        pbLoad?.progressProperty()?.bind(service.progressProperty())

        service.setOnSucceeded {
            exibeAlertaFinal()
            pbLoad?.opacity = 0.0
            btnSalvarArquivo?.isDisable = false
            vbFormulario?.isDisable = false
        }

        service.start()

    }

    private fun salvarArquivo() {
        val criar = Criar()

        if(!verificarCampos()) exibeAlertaFormulario().also { return }

        val rotulos = montarRotulos()

        val file = chamaSaveDialog(criar.nomeArquivo(rotulos[0]))

        file ?: return

        gerarRotulo(file.absolutePath, rotulos)

    }


    override fun initialize(location: URL?, resources: ResourceBundle?) {

        btnLimpaCampo?.setOnAction { limparCampos() }

        btnSalvarArquivo?.setOnAction { salvarArquivo() }

    }
}