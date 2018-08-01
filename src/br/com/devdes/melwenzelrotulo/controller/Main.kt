package br.com.devdes.melwenzelrotulo.controller

import br.com.devdes.melwenzelrotulo.model.Criar
import br.com.devdes.melwenzelrotulo.model.Rotulo

class Main {
    init {
        Criar().pdfRotulo(Rotulo())
    }
}