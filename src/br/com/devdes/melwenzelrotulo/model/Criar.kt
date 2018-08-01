package br.com.devdes.melwenzelrotulo.model

import com.itextpdf.awt.PdfGraphics2D
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import java.awt.Color
import java.awt.Font
import java.io.FileOutputStream
import java.nio.file.Paths
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Criar {

    private val paginaTamanho = PageSize.A5.rotate()

    private val fonteTamanhoInfo = 12
    private val fonteNome = "Arial"

    private val fonteNormalCorpo = Font(fonteNome, Font.PLAIN, fonteTamanhoInfo)
    private val fonteNegritoCorpo = Font(fonteNome, Font.BOLD, fonteTamanhoInfo)

    private val fonteNegritoCorpo2 = Font(fonteNome, Font.BOLD, fonteTamanhoInfo + 6)

    private val fonteNegritoTitulo = Font(fonteNome, Font.BOLD, fonteTamanhoInfo + 10)

    private val alinhamentoTopo = 273
    private val alinhamentoEsquerda = 16

    private fun formataLote(int: Int) : String {
        return int.toString().padStart(6, '0')
    }

    private fun formataDrum(int: Int) : String {
        return int.toString().padStart(2, '0')
    }

    private fun formataData(date: Date, paraArquivo : Boolean = false) : String {
        return SimpleDateFormat(if(!paraArquivo) "dd/mm/yyyy" else "dd-mm-yyyy-HH-mm-ss").format(date)
    }

    private fun formataNumDecimal(float: Float) : String {
        return DecimalFormat("##,##0.00").format(float)
    }

    private fun nomeArquivo(rotulo: Rotulo) : String {
        return "Rótulo - LOTE [${formataLote(rotulo.lote)}] - DATA DE CRIAÇÂO [${formataData(Date(), true)}].pdf"
    }

    private fun imagemBgParaAlinhamento(canvas : PdfContentByte) {

        val image = Image.getInstance(
                this.javaClass.getResource("${ Util().pathPackage() }/assets/rotulo.png")
        )

        image.scaleAbsolute(paginaTamanho)

        image.setAbsolutePosition(0f,0f)

        canvas.addImage(image)
    }

    private fun campoInformacaoGeral(
            g2d : PdfGraphics2D,
            drum : Int = 0,
            lot: Int = 1000,
            flowering: String = "flowering",
            datePacking: Date = Date()
    ) {

        val strings = listOf("Drum:", "Lot:", "Flowering:", "Date of Packing:")

        val espacoH = 4
        val espacoW = 4

        g2d.color = Color.BLACK

        g2d.font = fonteNormalCorpo

        g2d.drawString(strings[0], alinhamentoEsquerda, alinhamentoTopo)
        g2d.drawString(strings[1], alinhamentoEsquerda, alinhamentoTopo + fonteTamanhoInfo + espacoH)
        g2d.drawString(strings[2], alinhamentoEsquerda, alinhamentoTopo + (fonteTamanhoInfo * 2) + (espacoH * 2))
        g2d.drawString(strings[3], alinhamentoEsquerda, alinhamentoTopo + (fonteTamanhoInfo * 3) + (espacoH * 3))

        g2d.font = fonteNegritoCorpo

        g2d.drawString(formataDrum(drum), alinhamentoEsquerda + espacoW + 31, alinhamentoTopo)
        g2d.drawString(formataLote(lot), alinhamentoEsquerda + espacoW + 19, alinhamentoTopo + fonteTamanhoInfo + espacoH)
        g2d.drawString(flowering, alinhamentoEsquerda + espacoW + 53, alinhamentoTopo + (fonteTamanhoInfo * 2) + (espacoH * 2))
        g2d.drawString(formataData(datePacking), alinhamentoEsquerda + espacoW + 86, alinhamentoTopo + (fonteTamanhoInfo * 3) + (espacoH * 3))
    }

    private fun campoValidade(g2d : PdfGraphics2D, validity : Date = Date()) {

        val espacoH = 113
        val espacoW = 44

        g2d.color = Color.BLACK

        g2d.font = fonteNegritoCorpo2

        g2d.drawString(formataData(validity), alinhamentoEsquerda + espacoW, alinhamentoTopo + espacoH)
    }

    private fun campoPeso(
            g2d : PdfGraphics2D,
            grossWeight : Float = 0.0f,
            netWeight : Float = 0.0f
    ) {

        val strings = listOf("Gross Weight:", "Net Weight:")
        val espacoH = 95
        val espacoW = 210

        val distancia = 33

        val espacoEntreTexto = 4

        val centralizacao = 3

        g2d.color = Color.BLACK

        g2d.font = fonteNegritoCorpo

        g2d.drawString(strings[0], alinhamentoEsquerda + espacoW, alinhamentoTopo + espacoH)
        g2d.drawString(strings[1], alinhamentoEsquerda + espacoW, alinhamentoTopo + espacoH + distancia)

        g2d.font = fonteNegritoTitulo

        g2d.drawString(
                formataNumDecimal(grossWeight),
                alinhamentoEsquerda + espacoW + espacoEntreTexto + 80,
                alinhamentoTopo + espacoH + centralizacao
        )

        g2d.drawString(
                formataNumDecimal(netWeight),
                alinhamentoEsquerda + espacoW  + espacoEntreTexto + 65,
                alinhamentoTopo + espacoH + distancia + centralizacao
        )
    }

    private fun alteraCanvasDocumento(canvas: PdfContentByte, indice : Int, rotulo: Rotulo) {

        val template = canvas.createTemplate(paginaTamanho.width, paginaTamanho.height)
        val g2d = PdfGraphics2D(template, paginaTamanho.width, paginaTamanho.height)

        campoInformacaoGeral(g2d, indice + 1, rotulo.lote, rotulo.floracao, rotulo.dataEmpacotamento)

        campoValidade(g2d, rotulo.validade)

        campoPeso(g2d, rotulo.pesoBruto, rotulo.pesoLiquido)

        g2d.dispose()

        canvas.addTemplate(template, 0f, 0f)
    }

    private fun pdfRotuloDocumento(
            rotulos: ArrayList<Rotulo>,
            caminho : String,
            imagemFundo : Boolean = false
    ) : Document {

        val document = Document(paginaTamanho)
        val writer = PdfWriter.getInstance(document, FileOutputStream(Paths.get(caminho, nomeArquivo(rotulos[0])).toFile()))

        document.open()

        val canvas = writer.directContent


        for ((i, rotulo) in rotulos.withIndex()) {

            if(imagemFundo) imagemBgParaAlinhamento(canvas)

            alteraCanvasDocumento(canvas, i, rotulo)

            document.newPage()
        }

        return document
    }

    fun pdfRotulo(rotulo: Rotulo, caminho : String = "", imagemFundo : Boolean = false) {
        pdfRotuloDocumento(arrayListOf(rotulo), caminho, imagemFundo).close()
    }

    fun pdfRotulos(rotulos: ArrayList<Rotulo>, caminho : String = "", imagemFundo : Boolean = false) {
        pdfRotuloDocumento(rotulos, caminho, imagemFundo).close()
    }

}