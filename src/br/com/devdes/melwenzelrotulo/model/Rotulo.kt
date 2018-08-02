package br.com.devdes.melwenzelrotulo.model

import java.util.*

class Rotulo {
    var lote : Int = 0
    var floracao : String = "Não Informado"
    var dataEmpacotamento : Date = Date()
    var pesoBruto : Float = 0.0f
    var pesoLiquido : Float = 0.0f
    var validade : Date = Date()

    override fun toString(): String {
        return "Rotulo(lote=$lote, floracao='$floracao', dataEmpacotamento=$dataEmpacotamento, pesoBruto=$pesoBruto, pesoLiquido=$pesoLiquido, validade=$validade)"
    }


}