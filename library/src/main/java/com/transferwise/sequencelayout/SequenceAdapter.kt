package com.transferwise.sequencelayout

/**
 * Adapter to bind data to [com.transferwise.sequencelayout.SequenceStepView]s for a [com.transferwise.sequencelayout.SequenceLayoutView].
 */
abstract class SequenceAdapter<T> where T : Any {

    abstract fun getCount(): Int

    abstract fun getItem(position: Int): T

    abstract fun bindView(sequenceStep: SequenceStepView, item: T)
}
