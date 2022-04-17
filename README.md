# CardStack

[![](https://jitpack.io/v/omercemcicekli/CardStack.svg)](https://jitpack.io/#omercemcicekli/CardStack)

A hobby project of mine needed Jetpack Compose Cards stacked on top of another. As I finished up the code, I decided to polish and release it as a library so here it is.

### Usage

You can define a composable and create as many cards as you want by giving a card count.

```kotlin
val drawables = listOf(R.drawable.first, R.drawable.second, R.drawable.third, R.drawable.fourth)

CardStack({ index -> 
	Image(painterResource(id = drawables[index]),
              contentDescription = "Same Card Type with Different Image",
              contentScale = ContentScale.Crop,
              modifier = Modifier.size(196.dp, 196.dp)) },
   	cardCount = drawables.size)
    
```

![Alt Text](https://media.giphy.com/media/4XHSwUus1A71tOOnnA/giphy.gif)

You can also define list of composables and create different card layouts

```kotlin
CardStack(listOf(
	{
		Text(text = "First Card", 
		     textAlign = TextAlign.Center, 
		     modifier = Modifier.size(196.dp)
	},
   	{
       		Image(painterResource(id = R.drawable.second),
                      contentDescription = "Second Card Image",
                      contentScale = ContentScale.Crop,
                      modifier = Modifier.size(196.dp))
   	}, 
   	{
   		Column(horizontalAlignment = Alignment.CenterHorizontally, 
   				modifier = Modifier.size(196.dp)) {
                   Text(text = "Third Card With Button", 
                   		textAlign = TextAlign.Center)
                   Button(onClick = {}) { Text(text = "Button Text") } 
             }
   	},
   	{
   		Image(painterResource(id = R.drawable.fourth),
                      contentDescription = "Fourth Card Image",
                      contentScale = ContentScale.Crop,
                      modifier = Modifier.size(196.dp)) })
)
```
                             
![Alt Text](https://media.giphy.com/media/0csGgiP6l8tqVTGa6H/giphy.gif)

                         
### Customization

You can define;

* Card shape
* Card border
* Card elevation
* Padding between cards
* Animation duration (You can disable them completely by assigning 0)
* Orientation

```kotlin
	CardStack(
        	...,
	        cardShape = CircleShape, 
        	cardBorder = BorderStroke(2.dp, Color.White),
	        cardElevation = 10.dp,
	        paddingBetweenCards = 10.dp,
        	animationDuration = 250,
	        orientation = Orientation.Horizontal(alignment = HorizontalAlignment.EndToStart,
                                             animationStyle = HorizontalAnimationStyle.FromBottom)
   	)
```
	
![Alt Text](https://media.giphy.com/media/OyIkBjyyKSJ2VJTDQo/giphy.gif)

### Get the Library

<pre>
<code>
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
	</code></pre>

<pre>
<code>
implementation 'com.github.omercemcicekli:CardStack:0.0.3'
	</code></pre>

### License

<pre>
<code>
The MIT License (MIT)
	
Copyright (c) 2022 Omer Cem Cicekli
	
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
	
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
	
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
	</code></pre>



