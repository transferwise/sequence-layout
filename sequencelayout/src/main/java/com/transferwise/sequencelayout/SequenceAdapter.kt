package com.transferwise.sequencelayout

/**
 * Adapter to bind data to [com.transferwise.sequencelayout.SequenceStep]s for a [com.transferwise.sequencelayout.SequenceLayout].
 */
abstract class SequenceAdapter<T> {
    
    fun bindView(SequenceStep: SequenceStep, position: Int) {
        bindView(SequenceStep, getItem(position))
    }

    abstract fun getCount(): Int

    abstract fun bindView(SequenceStep: SequenceStep, item: T)

    abstract fun newView(parent: SequenceLayout): SequenceStep

    abstract fun getItem(position: Int): T
}
