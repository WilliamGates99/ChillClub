package com.xeniac.chillclub.core.domain.utils

abstract class ResultError {
    object BlankField : ResultError()
    object UncheckedRadioButton : ResultError()
    object UncheckedCheckBox : ResultError()
}