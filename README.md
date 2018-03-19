# sequence-layout
<img align="right" src="https://media.giphy.com/media/TGaDOPfTrX749uhD0L/giphy.gif">
A vertical sequence UI component for Android

## Setup
`TODO: explain how to add maven dependency`

## Usage

Take a look at the `sample` app in this repository.

### In XML layout
You can define steps in your XML layout:

```xml
<com.transferwise.sequencelayout.SequenceLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <com.transferwise.sequencelayout.SequenceStep
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:anchor="30 Nov"
        app:title="First step"/>

    <com.transferwise.sequencelayout.SequenceStep
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:title="Second step"/>

    <com.transferwise.sequencelayout.SequenceStep
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:active="true"
        app:anchor="Today"
        app:subtitle="Subtitle of this step."
        app:title="Third step"/>

    <com.transferwise.sequencelayout.SequenceStep
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:title="Fourth step"/>

    <com.transferwise.sequencelayout.SequenceStep
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:anchor="2 Dec"
        app:title="Fifth step"/>
</com.transferwise.sequencelayout.SequenceLayout>
```

Available attributes for `SequenceLayout`:

| Attribute | Description |
| --- | --- |
| `activeColor` | foreground color of the progress bar |
| `backgroundColor` | background color of the progress bar |

Available attributes for `SequenceStep`:

| Attribute | Description |
| --- | --- |
| `active` | boolean to indicate if step is active |
| `anchor` | text for the left side of the step |
| `anchorTextAppearance` | styling for the left side of the step |
| `title` | title of the step |
| `titleTextAppearance` | styling for the title of the step |
| `title` | subtitle of the step |
| `titleTextAppearance` | styling for the subtitle of the step |

### Programmatically

Alternatively, define an adapter that extends `SequenceAdapter<T>`, like this:

```kotlin
class MyAdapter(private val items: List<MyItem>) : SequenceAdapter<MyItem>() {

    override fun getCount(): Int {
        items.size
    }

    override fun getItem(position: Int): MyItem {
        items[position]
    }

    override fun bindView(sequenceStep: SequenceStep, item: MyItem) {
        with(sequenceStep) {
            setActive(item.isActive)
            setAnchor(item.formattedDate)
            setAnchorTextAppearance(...)
            setTitle(item.title)
            setTitleTextAppearance()
            setSubtitle(...)
            setSubtitleTextAppearance(...)
        }
    }

    data class MyItem(val isActive: Boolean,
                      val formattedDate: String,
                      val title: String)
}
```

... and use it for a `SequenceLayout`:

```kotlin
val items = listOf(MyItem(...), MyItem(...), MyItem(...))
sequenceLayout.setAdapter(MyAdapter(items))
```
