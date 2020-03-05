
# Tech Assignment Documentation for Sellics

### Estimation Calculation Logic

I implemented two approaches for estimation calculation. On each approach, I choose the way of brutal requesting by prefixing the search keyword with sequential keyword letters.
> Consider we are requesting for **iphone** keyword,
Our algorithm will call the Amazon API with keywords simultaneously:
**i**, **ip**, **iph**, **ipho**, **iphon**, **iphone**

After collecting the results, we can calculate the estimation rate in two approaches as, 
- **Simple Calculation Estimation**
This approach considers the exact matching of keyword and the suggestion index of Amazon API. We will give values to the suggestions regarding their index values between 0 - 100. Then calculate the average rate after collect all the ranking values. As for the example above, results will be like this.
> When we search for the keyword **i** the Amazon API will return these suggestions. As we can see there is not an exact matching for our **iphone** keyword. So the rate will be 0 (zero). 
> *"iphone 11 case", "iphone charger", "iphone 11 screen protector"," iphone 11 pro max case", "iphone xr cases", "ipad", "iphone 8 case", "iphone 8 plus case", "iphone 7 case", "iphone xr screen protector"*
> 
>After requesting for all the words prefixed by the **iphone** letters and collecting the ratings, our total rate will be calculated as:
>(0 + 0 + 0 + 0 + 0 + 0) / 6 = **0**;
>
>If we were looking for the **ipad** word the result would be like
>>**i -** *"iphone 11 case", "iphone charger", "iphone 11 screen protector"," iphone 11 pro max case", "iphone xr cases", "**ipad**", "iphone 8 case", "iphone 8 plus case", "iphone 7 case", "iphone xr screen protector"*
>>
>>**ip-** *"iphone 11 case", "iphone charger", "iphone 11 screen protector", "iphone 11 pro max case", "iphone xr cases", "**ipad**", "iphone 8 case", "iphone 8 plus case", "iphone 7 case", "iphone xr screen protector"*
>>
>>**ipa-** "**ipad**", "ipad 7th generation case", "ipad 7th generation screen protector", "ipad 10.2 case", "ipad air 2 case", "ipad pro", "ipad pro 11 case", "ipad mini", "ipad mini 2 case", "ipad 6th generation cases"
>>
>>**ipad-** "**ipad**","ipad 7th generation case","ipad 7th generation screen protector","ipad 10.2 case","ipad air 2 case","ipad pro","ipad pro 11 case","ipad mini","ipad mini 2 case","ipad 6th generation cases"
>
>So by collecting and calculating the ranks by their index values and dividing the total request number, we will achieve the score as 
> (50 + 50 + 100 + 100) / 4 = **75**


- **Aggregated Calculation Estimation**
This is an alternate estimation score calculating method. The reason why I implemented this algorithm is, as we can see from the examples above, when we search for the **iphone** word, all the suggestions returned from Amazon API is related to the **iphone** product. Some are the models of cellular, and some are the accessories produced for iphone. So that means, iphone is so popular as a product, and people frequently searching for iphone or its accessories. But due to the exact matching limitation, our popularity algorithm calculates the score as **0**
On this approach we will take the related  suggestions into account. So if the suggestion supplied by Amazon contains an exact matching, we will assume its rank as we calculated in **Simple Calculation Estimation**. If there is not an exact match, but the suggestions contains all the words in our keyword, we will give value to each suggestion, and calculate the average rate of that suggestion list.
As for the same example, 

>**i -** *"**iphone** 11 case", "**iphone** charger", "**iphone** 11 screen protector"," **iphone** 11 pro max case", "**iphone** xr cases", "ipad*", "**iphone** 8 case", "**iphone** 8 plus case", "**iphone** 7 case", "**iphone** xr screen protector"*
>
>See that **9 of 10** suggestions contains the word as **iphone** but there is not any exact match. So we will give values to the suggestions regarding their values, and calculate the average percentile.
>100 + 90 +  80 + 70 + 60 + 40 + 30 + 20 + 10 = **500**
>Total available value = **550** (by adding the missing 50 because of ipad suggestion)
> And our rate is 500 /550 = **~90**
>
>After applying this approach to every requests made by the prefixes of iphone word, we will reach an estimated score of: 
> `{	"keyword": "iphone",	"score": 96	}`

>**Important Hint:** To calculate the score in aggregated way, you should add an extra estimation=aggregated parameter to query. Otherwise, the algorithm will calculate in Basic Estimation which is asked for exact match by default.


### - Do you think the (​*hint​) that we gave you earlier is correct and if so - why?
The response to this question changes regarding to the approach.

- For **Basic Estimation Approach** this is **false**, because as we can see from the suggestions for ipad word, by the increasing of matching prefix on query, Amazon takes the most relevant upper.

- For **Aggregated Estimation Approach** this can be assumed **true**, because as we again see from the suggestions of **iphone** word, Amazon suggests some iphone related words, then an **ipad word in the middle**, and **more iphone related words** again.

### -How precise do you think your outcome is and why?
Actually if we just have a concern about exact keyword matching, first solution gives some clue, but is not adequate. I think we need to have a concern about the real customer experience results, and the second approach is better. But it should be tested with wide range of data to have remarkable result. By the way, it should be divided into sections to query the different markets of Amazon to gather more accurate data. I believe nothing is perfect and everything  can be improved :)

## Screenshots
![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/1.PNG)

![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/2.PNG)

![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/3.PNG)

![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/4.PNG)

![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/5.PNG)

![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/6.PNG)

![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/7.PNG)

![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/8.PNG)

![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/9.PNG)

![](https://raw.githubusercontent.com/uprockin/amazon-autocomplete-api/master/screenshots/10.PNG)
