package br.com.devdes.melwenzelrotulo.controller

import br.com.devdes.melwenzelrotulo.model.Criar
import br.com.devdes.melwenzelrotulo.model.Rotulo
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import java.net.URL
import java.util.*

class Main : Initializable{

    @FXML var tfQtdRotulo : TextField? = null
    @FXML var tfLote : TextField? = null
    @FXML var tfFloracao : TextField? = null
    @FXML var tfPesoBruto : TextField? = null
    @FXML var tfPesoLiquido : TextField? = null

    @FXML var cbImagemFundo : CheckBox? = null

    @FXML var dpDataEmpacotamento : DatePicker? = null
    @FXML var dpValidade : DatePicker? = null

    @FXML var lblDev : Label? = null

    @FXML var pbLoad : ProgressBar? = null

    @FXML var btnLimpaCampo : Button? = null
    @FXML var btnSalvarArquivo : Button? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        Criar().pdfRotulo(Rotulo(), "", true)
    }
}