import"./chunk-3U3256AC.js";import{a as ca,c as pa,d as ua}from"./chunk-IJJGKUZF.js";import{a as na}from"./chunk-5F4VW5KE.js";import"./chunk-Y6GX5UVG.js";import{a as ia,c as ra,m as oa,n as sa,o as da,q as la}from"./chunk-GED3EX5Q.js";import{$a as R,$c as Ot,Aa as h,Ba as s,Ca as d,Cd as Wt,Da as w,Dd as jt,Ea as V,Eb as ft,Ec as xt,Ed as Kt,F as P,Fa as I,G as ce,Ga as rt,Gb as bt,Gc as Vt,Gd as qt,H as ee,Ha as O,Hc as It,Hd as Ut,I as pe,Ia as L,Id as $t,J as p,Jd as Qt,Ka as N,Kc as Ie,Kd as Gt,L as b,La as U,Ld as Zt,M as D,Ma as g,Md as Xt,N as te,Na as ot,Nb as Dt,Nd as Jt,O as Ae,Oa as u,Od as ea,P as tt,Pa as Ee,Pc as Tt,Pd as ta,Qa as xe,Qb as vt,Qd as aa,Ra as st,S as m,Sa as K,T as Me,Ta as T,U as _,Ua as F,Vc as Ft,W as ae,Wa as ge,Wc as ie,Xa as Y,Y as ue,Ya as fe,Yc as re,Za as c,Zc as ve,_a as A,_b as yt,_c as Rt,a as Je,ab as Ve,b as et,bb as B,bc as Ct,cb as H,da as l,db as z,dd as Lt,ea as at,f as J,fb as dt,fd as Pt,gb as lt,gc as wt,h as j,hc as At,hd as Nt,ia as ke,ib as ct,ic as Mt,ja as Se,ka as nt,kb as pt,m as de,ma as k,mb as ut,md as Yt,na as me,nb as mt,nd as Bt,oa as it,oc as kt,pb as W,qa as y,qb as ht,rd as Te,s as we,sc as be,sd as Ht,tc as St,ua as S,va as E,vd as zt,wa as x,xb as _t,xc as Et,y as le,ya as he,yc as ne,za as _e,zb as gt,zc as De}from"./chunk-YRE5MUFP.js";var Fe=new ee("MAT_DATE_LOCALE",{providedIn:"root",factory:()=>p(ut)}),$="Method not implemented",v=class{locale;_localeChanges=new j;localeChanges=this._localeChanges;setTime(r,e,t,a){throw new Error($)}getHours(r){throw new Error($)}getMinutes(r){throw new Error($)}getSeconds(r){throw new Error($)}parseTime(r,e){throw new Error($)}addSeconds(r,e){throw new Error($)}getValidDateOrNull(r){return this.isDateInstance(r)&&this.isValid(r)?r:null}deserialize(r){return r==null||this.isDateInstance(r)&&this.isValid(r)?r:this.invalid()}setLocale(r){this.locale=r,this._localeChanges.next()}compareDate(r,e){return this.getYear(r)-this.getYear(e)||this.getMonth(r)-this.getMonth(e)||this.getDate(r)-this.getDate(e)}compareTime(r,e){return this.getHours(r)-this.getHours(e)||this.getMinutes(r)-this.getMinutes(e)||this.getSeconds(r)-this.getSeconds(e)}sameDate(r,e){if(r&&e){let t=this.isValid(r),a=this.isValid(e);return t&&a?!this.compareDate(r,e):t==a}return r==e}sameTime(r,e){if(r&&e){let t=this.isValid(r),a=this.isValid(e);return t&&a?!this.compareTime(r,e):t==a}return r==e}clampDate(r,e,t){return e&&this.compareDate(r,e)<0?e:t&&this.compareDate(r,t)>0?t:r}},q=new ee("mat-date-formats");var Va=["mat-calendar-body",""];function Ia(n,r){return this._trackRow(r)}var ba=(n,r)=>r.id;function Ta(n,r){if(n&1&&(V(0,"tr",0)(1,"td",3),c(2),I()()),n&2){let e=u();l(),ge("padding-top",e._cellPadding)("padding-bottom",e._cellPadding),S("colspan",e.numCols),l(),R(" ",e.label," ")}}function Fa(n,r){if(n&1&&(V(0,"td",3),c(1),I()),n&2){let e=u(2);ge("padding-top",e._cellPadding)("padding-bottom",e._cellPadding),S("colspan",e._firstRowOffset),l(),R(" ",e._firstRowOffset>=e.labelMinRequiredCells?e.label:""," ")}}function Ra(n,r){if(n&1){let e=N();V(0,"td",6)(1,"button",7),ot("click",function(a){let i=b(e).$implicit,o=u(2);return D(o._cellClicked(i,a))})("focus",function(a){let i=b(e).$implicit,o=u(2);return D(o._emitActiveDateChange(i,a))}),V(2,"span",8),c(3),I(),rt(4,"span",9),I()()}if(n&2){let e=r.$implicit,t=r.$index,a=u().$index,i=u();ge("width",i._cellWidth)("padding-top",i._cellPadding)("padding-bottom",i._cellPadding),S("data-mat-row",a)("data-mat-col",t),l(),fe(e.cssClasses),Y("mat-calendar-body-disabled",!e.enabled)("mat-calendar-body-active",i._isActiveCell(a,t))("mat-calendar-body-range-start",i._isRangeStart(e.compareValue))("mat-calendar-body-range-end",i._isRangeEnd(e.compareValue))("mat-calendar-body-in-range",i._isInRange(e.compareValue))("mat-calendar-body-comparison-bridge-start",i._isComparisonBridgeStart(e.compareValue,a,t))("mat-calendar-body-comparison-bridge-end",i._isComparisonBridgeEnd(e.compareValue,a,t))("mat-calendar-body-comparison-start",i._isComparisonStart(e.compareValue))("mat-calendar-body-comparison-end",i._isComparisonEnd(e.compareValue))("mat-calendar-body-in-comparison-range",i._isInComparisonRange(e.compareValue))("mat-calendar-body-preview-start",i._isPreviewStart(e.compareValue))("mat-calendar-body-preview-end",i._isPreviewEnd(e.compareValue))("mat-calendar-body-in-preview",i._isInPreview(e.compareValue)),U("tabIndex",i._isActiveCell(a,t)?0:-1),S("aria-label",e.ariaLabel)("aria-disabled",!e.enabled||null)("aria-pressed",i._isSelected(e.compareValue))("aria-current",i.todayValue===e.compareValue?"date":null)("aria-describedby",i._getDescribedby(e.compareValue)),l(),Y("mat-calendar-body-selected",i._isSelected(e.compareValue))("mat-calendar-body-comparison-identical",i._isComparisonIdentical(e.compareValue))("mat-calendar-body-today",i.todayValue===e.compareValue),l(),R(" ",e.displayValue," ")}}function Oa(n,r){if(n&1&&(V(0,"tr",1),E(1,Fa,2,6,"td",4),he(2,Ra,5,49,"td",5,ba),I()),n&2){let e=r.$implicit,t=r.$index,a=u();l(),x(t===0&&a._firstRowOffset?1:-1),l(),_e(e)}}function La(n,r){if(n&1&&(s(0,"th",2)(1,"span",6),c(2),d(),s(3,"span",3),c(4),d()()),n&2){let e=r.$implicit;l(2),A(e.long),l(2),A(e.narrow)}}var Pa=["*"];function Na(n,r){}function Ya(n,r){if(n&1){let e=N();s(0,"mat-month-view",4),z("activeDateChange",function(a){b(e);let i=u();return H(i.activeDate,a)||(i.activeDate=a),D(a)}),g("_userSelection",function(a){b(e);let i=u();return D(i._dateSelected(a))})("dragStarted",function(a){b(e);let i=u();return D(i._dragStarted(a))})("dragEnded",function(a){b(e);let i=u();return D(i._dragEnded(a))}),d()}if(n&2){let e=u();B("activeDate",e.activeDate),h("selected",e.selected)("dateFilter",e.dateFilter)("maxDate",e.maxDate)("minDate",e.minDate)("dateClass",e.dateClass)("comparisonStart",e.comparisonStart)("comparisonEnd",e.comparisonEnd)("startDateAccessibleName",e.startDateAccessibleName)("endDateAccessibleName",e.endDateAccessibleName)("activeDrag",e._activeDrag)}}function Ba(n,r){if(n&1){let e=N();s(0,"mat-year-view",5),z("activeDateChange",function(a){b(e);let i=u();return H(i.activeDate,a)||(i.activeDate=a),D(a)}),g("monthSelected",function(a){b(e);let i=u();return D(i._monthSelectedInYearView(a))})("selectedChange",function(a){b(e);let i=u();return D(i._goToDateInView(a,"month"))}),d()}if(n&2){let e=u();B("activeDate",e.activeDate),h("selected",e.selected)("dateFilter",e.dateFilter)("maxDate",e.maxDate)("minDate",e.minDate)("dateClass",e.dateClass)}}function Ha(n,r){if(n&1){let e=N();s(0,"mat-multi-year-view",6),z("activeDateChange",function(a){b(e);let i=u();return H(i.activeDate,a)||(i.activeDate=a),D(a)}),g("yearSelected",function(a){b(e);let i=u();return D(i._yearSelectedInMultiYearView(a))})("selectedChange",function(a){b(e);let i=u();return D(i._goToDateInView(a,"year"))}),d()}if(n&2){let e=u();B("activeDate",e.activeDate),h("selected",e.selected)("dateFilter",e.dateFilter)("maxDate",e.maxDate)("minDate",e.minDate)("dateClass",e.dateClass)}}function za(n,r){}var Wa=["button"],ja=[[["","matDatepickerToggleIcon",""]]],Ka=["[matDatepickerToggleIcon]"];function qa(n,r){n&1&&(te(),s(0,"svg",2),w(1,"path",3),d())}var X=(()=>{class n{changes=new j;calendarLabel="Calendar";openCalendarLabel="Open calendar";closeCalendarLabel="Close calendar";prevMonthLabel="Previous month";nextMonthLabel="Next month";prevYearLabel="Previous year";nextYearLabel="Next year";prevMultiYearLabel="Previous 24 years";nextMultiYearLabel="Next 24 years";switchToMonthViewLabel="Choose date";switchToMultiYearViewLabel="Choose month and year";startDateLabel="Start date";endDateLabel="End date";comparisonDateLabel="Comparison range";formatYearRange(e,t){return`${e} \u2013 ${t}`}formatYearRangeLabel(e,t){return`${e} to ${t}`}static \u0275fac=function(t){return new(t||n)};static \u0275prov=P({token:n,factory:n.\u0275fac,providedIn:"root"})}return n})(),Ua=0,se=class{value;displayValue;ariaLabel;enabled;compareValue;rawValue;id=Ua++;cssClasses;constructor(r,e,t,a,i,o=r,f){this.value=r,this.displayValue=e,this.ariaLabel=t,this.enabled=a,this.compareValue=o,this.rawValue=f,this.cssClasses=i instanceof Set?Array.from(i):i}},$a={passive:!1,capture:!0},ye={passive:!0,capture:!0},ma={passive:!0},Z=(()=>{class n{_elementRef=p(ue);_ngZone=p(Me);_platform=p(Mt);_intl=p(X);_eventCleanups;_skipNextFocus=!1;_focusActiveCellAfterViewChecked=!1;label;rows;todayValue;startValue;endValue;labelMinRequiredCells;numCols=7;activeCell=0;ngAfterViewChecked(){this._focusActiveCellAfterViewChecked&&(this._focusActiveCell(),this._focusActiveCellAfterViewChecked=!1)}isRange=!1;cellAspectRatio=1;comparisonStart=null;comparisonEnd=null;previewStart=null;previewEnd=null;startDateAccessibleName=null;endDateAccessibleName=null;selectedValueChange=new m;previewChange=new m;activeDateChange=new m;dragStarted=new m;dragEnded=new m;_firstRowOffset;_cellPadding;_cellWidth;_startDateLabelId;_endDateLabelId;_comparisonStartDateLabelId;_comparisonEndDateLabelId;_didDragSinceMouseDown=!1;_injector=p(tt);comparisonDateAccessibleName=this._intl.comparisonDateLabel;_trackRow=e=>e;constructor(){let e=p(ke),t=p(Ie);this._startDateLabelId=t.getId("mat-calendar-body-start-"),this._endDateLabelId=t.getId("mat-calendar-body-end-"),this._comparisonStartDateLabelId=t.getId("mat-calendar-body-comparison-start-"),this._comparisonEndDateLabelId=t.getId("mat-calendar-body-comparison-end-"),p(ne).load(Ft),this._ngZone.runOutsideAngular(()=>{let a=this._elementRef.nativeElement,i=[e.listen(a,"touchmove",this._touchmoveHandler,$a),e.listen(a,"mouseenter",this._enterHandler,ye),e.listen(a,"focus",this._enterHandler,ye),e.listen(a,"mouseleave",this._leaveHandler,ye),e.listen(a,"blur",this._leaveHandler,ye),e.listen(a,"mousedown",this._mousedownHandler,ma),e.listen(a,"touchstart",this._mousedownHandler,ma)];this._platform.isBrowser&&i.push(e.listen("window","mouseup",this._mouseupHandler),e.listen("window","touchend",this._touchendHandler)),this._eventCleanups=i})}_cellClicked(e,t){this._didDragSinceMouseDown||e.enabled&&this.selectedValueChange.emit({value:e.value,event:t})}_emitActiveDateChange(e,t){e.enabled&&this.activeDateChange.emit({value:e.value,event:t})}_isSelected(e){return this.startValue===e||this.endValue===e}ngOnChanges(e){let t=e.numCols,{rows:a,numCols:i}=this;(e.rows||t)&&(this._firstRowOffset=a&&a.length&&a[0].length?i-a[0].length:0),(e.cellAspectRatio||t||!this._cellPadding)&&(this._cellPadding=`${50*this.cellAspectRatio/i}%`),(t||!this._cellWidth)&&(this._cellWidth=`${100/i}%`)}ngOnDestroy(){this._eventCleanups.forEach(e=>e())}_isActiveCell(e,t){let a=e*this.numCols+t;return e&&(a-=this._firstRowOffset),a==this.activeCell}_focusActiveCell(e=!0){at(()=>{setTimeout(()=>{let t=this._elementRef.nativeElement.querySelector(".mat-calendar-body-active");t&&(e||(this._skipNextFocus=!0),t.focus())})},{injector:this._injector})}_scheduleFocusActiveCellAfterViewChecked(){this._focusActiveCellAfterViewChecked=!0}_isRangeStart(e){return Le(e,this.startValue,this.endValue)}_isRangeEnd(e){return Pe(e,this.startValue,this.endValue)}_isInRange(e){return Ne(e,this.startValue,this.endValue,this.isRange)}_isComparisonStart(e){return Le(e,this.comparisonStart,this.comparisonEnd)}_isComparisonBridgeStart(e,t,a){if(!this._isComparisonStart(e)||this._isRangeStart(e)||!this._isInRange(e))return!1;let i=this.rows[t][a-1];if(!i){let o=this.rows[t-1];i=o&&o[o.length-1]}return i&&!this._isRangeEnd(i.compareValue)}_isComparisonBridgeEnd(e,t,a){if(!this._isComparisonEnd(e)||this._isRangeEnd(e)||!this._isInRange(e))return!1;let i=this.rows[t][a+1];if(!i){let o=this.rows[t+1];i=o&&o[0]}return i&&!this._isRangeStart(i.compareValue)}_isComparisonEnd(e){return Pe(e,this.comparisonStart,this.comparisonEnd)}_isInComparisonRange(e){return Ne(e,this.comparisonStart,this.comparisonEnd,this.isRange)}_isComparisonIdentical(e){return this.comparisonStart===this.comparisonEnd&&e===this.comparisonStart}_isPreviewStart(e){return Le(e,this.previewStart,this.previewEnd)}_isPreviewEnd(e){return Pe(e,this.previewStart,this.previewEnd)}_isInPreview(e){return Ne(e,this.previewStart,this.previewEnd,this.isRange)}_getDescribedby(e){if(!this.isRange)return null;if(this.startValue===e&&this.endValue===e)return`${this._startDateLabelId} ${this._endDateLabelId}`;if(this.startValue===e)return this._startDateLabelId;if(this.endValue===e)return this._endDateLabelId;if(this.comparisonStart!==null&&this.comparisonEnd!==null){if(e===this.comparisonStart&&e===this.comparisonEnd)return`${this._comparisonStartDateLabelId} ${this._comparisonEndDateLabelId}`;if(e===this.comparisonStart)return this._comparisonStartDateLabelId;if(e===this.comparisonEnd)return this._comparisonEndDateLabelId}return null}_enterHandler=e=>{if(this._skipNextFocus&&e.type==="focus"){this._skipNextFocus=!1;return}if(e.target&&this.isRange){let t=this._getCellFromElement(e.target);t&&this._ngZone.run(()=>this.previewChange.emit({value:t.enabled?t:null,event:e}))}};_touchmoveHandler=e=>{if(!this.isRange)return;let t=ha(e),a=t?this._getCellFromElement(t):null;t!==e.target&&(this._didDragSinceMouseDown=!0),Oe(e.target)&&e.preventDefault(),this._ngZone.run(()=>this.previewChange.emit({value:a?.enabled?a:null,event:e}))};_leaveHandler=e=>{this.previewEnd!==null&&this.isRange&&(e.type!=="blur"&&(this._didDragSinceMouseDown=!0),e.target&&this._getCellFromElement(e.target)&&!(e.relatedTarget&&this._getCellFromElement(e.relatedTarget))&&this._ngZone.run(()=>this.previewChange.emit({value:null,event:e})))};_mousedownHandler=e=>{if(!this.isRange)return;this._didDragSinceMouseDown=!1;let t=e.target&&this._getCellFromElement(e.target);!t||!this._isInRange(t.compareValue)||this._ngZone.run(()=>{this.dragStarted.emit({value:t.rawValue,event:e})})};_mouseupHandler=e=>{if(!this.isRange)return;let t=Oe(e.target);if(!t){this._ngZone.run(()=>{this.dragEnded.emit({value:null,event:e})});return}t.closest(".mat-calendar-body")===this._elementRef.nativeElement&&this._ngZone.run(()=>{let a=this._getCellFromElement(t);this.dragEnded.emit({value:a?.rawValue??null,event:e})})};_touchendHandler=e=>{let t=ha(e);t&&this._mouseupHandler({target:t})};_getCellFromElement(e){let t=Oe(e);if(t){let a=t.getAttribute("data-mat-row"),i=t.getAttribute("data-mat-col");if(a&&i)return this.rows[parseInt(a)]?.[parseInt(i)]||null}return null}static \u0275fac=function(t){return new(t||n)};static \u0275cmp=k({type:n,selectors:[["","mat-calendar-body",""]],hostAttrs:[1,"mat-calendar-body"],inputs:{label:"label",rows:"rows",todayValue:"todayValue",startValue:"startValue",endValue:"endValue",labelMinRequiredCells:"labelMinRequiredCells",numCols:"numCols",activeCell:"activeCell",isRange:"isRange",cellAspectRatio:"cellAspectRatio",comparisonStart:"comparisonStart",comparisonEnd:"comparisonEnd",previewStart:"previewStart",previewEnd:"previewEnd",startDateAccessibleName:"startDateAccessibleName",endDateAccessibleName:"endDateAccessibleName"},outputs:{selectedValueChange:"selectedValueChange",previewChange:"previewChange",activeDateChange:"activeDateChange",dragStarted:"dragStarted",dragEnded:"dragEnded"},exportAs:["matCalendarBody"],features:[ae],attrs:Va,decls:11,vars:11,consts:[["aria-hidden","true"],["role","row"],[1,"mat-calendar-body-hidden-label",3,"id"],[1,"mat-calendar-body-label"],[1,"mat-calendar-body-label",3,"paddingTop","paddingBottom"],["role","gridcell",1,"mat-calendar-body-cell-container",3,"width","paddingTop","paddingBottom"],["role","gridcell",1,"mat-calendar-body-cell-container"],["type","button",1,"mat-calendar-body-cell",3,"click","focus","tabindex"],[1,"mat-calendar-body-cell-content","mat-focus-indicator"],["aria-hidden","true",1,"mat-calendar-body-cell-preview"]],template:function(t,a){t&1&&(E(0,Ta,3,6,"tr",0),he(1,Oa,4,1,"tr",1,Ia,!0),V(3,"span",2),c(4),I(),V(5,"span",2),c(6),I(),V(7,"span",2),c(8),I(),V(9,"span",2),c(10),I()),t&2&&(x(a._firstRowOffset<a.labelMinRequiredCells?0:-1),l(),_e(a.rows),l(2),U("id",a._startDateLabelId),l(),R(" ",a.startDateAccessibleName,`
`),l(),U("id",a._endDateLabelId),l(),R(" ",a.endDateAccessibleName,`
`),l(),U("id",a._comparisonStartDateLabelId),l(),Ve(" ",a.comparisonDateAccessibleName," ",a.startDateAccessibleName,`
`),l(),U("id",a._comparisonEndDateLabelId),l(),Ve(" ",a.comparisonDateAccessibleName," ",a.endDateAccessibleName,`
`))},styles:[`.mat-calendar-body {
  min-width: 224px;
}

.mat-calendar-body-today:not(.mat-calendar-body-selected):not(.mat-calendar-body-comparison-identical) {
  border-color: var(--mat-datepicker-calendar-date-today-outline-color, var(--mat-sys-primary));
}

.mat-calendar-body-label {
  height: 0;
  line-height: 0;
  text-align: start;
  padding-left: 4.7142857143%;
  padding-right: 4.7142857143%;
  font-size: var(--mat-datepicker-calendar-body-label-text-size, var(--mat-sys-title-small-size));
  font-weight: var(--mat-datepicker-calendar-body-label-text-weight, var(--mat-sys-title-small-weight));
  color: var(--mat-datepicker-calendar-body-label-text-color, var(--mat-sys-on-surface));
}

.mat-calendar-body-hidden-label {
  display: none;
}

.mat-calendar-body-cell-container {
  position: relative;
  height: 0;
  line-height: 0;
}

.mat-calendar-body-cell {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: none;
  text-align: center;
  outline: none;
  margin: 0;
  font-family: var(--mat-datepicker-calendar-text-font, var(--mat-sys-body-medium-font));
  font-size: var(--mat-datepicker-calendar-text-size, var(--mat-sys-body-medium-size));
  -webkit-user-select: none;
  user-select: none;
  cursor: pointer;
  outline: none;
  border: none;
  -webkit-tap-highlight-color: transparent;
}
.mat-calendar-body-cell::-moz-focus-inner {
  border: 0;
}

.mat-calendar-body-cell::before,
.mat-calendar-body-cell::after,
.mat-calendar-body-cell-preview {
  content: "";
  position: absolute;
  top: 5%;
  left: 0;
  z-index: 0;
  box-sizing: border-box;
  display: block;
  height: 90%;
  width: 100%;
}

.mat-calendar-body-range-start:not(.mat-calendar-body-in-comparison-range)::before,
.mat-calendar-body-range-start::after,
.mat-calendar-body-comparison-start:not(.mat-calendar-body-comparison-bridge-start)::before,
.mat-calendar-body-comparison-start::after,
.mat-calendar-body-preview-start .mat-calendar-body-cell-preview {
  left: 5%;
  width: 95%;
  border-top-left-radius: 999px;
  border-bottom-left-radius: 999px;
}
[dir=rtl] .mat-calendar-body-range-start:not(.mat-calendar-body-in-comparison-range)::before,
[dir=rtl] .mat-calendar-body-range-start::after,
[dir=rtl] .mat-calendar-body-comparison-start:not(.mat-calendar-body-comparison-bridge-start)::before,
[dir=rtl] .mat-calendar-body-comparison-start::after,
[dir=rtl] .mat-calendar-body-preview-start .mat-calendar-body-cell-preview {
  left: 0;
  border-radius: 0;
  border-top-right-radius: 999px;
  border-bottom-right-radius: 999px;
}

.mat-calendar-body-range-end:not(.mat-calendar-body-in-comparison-range)::before,
.mat-calendar-body-range-end::after,
.mat-calendar-body-comparison-end:not(.mat-calendar-body-comparison-bridge-end)::before,
.mat-calendar-body-comparison-end::after,
.mat-calendar-body-preview-end .mat-calendar-body-cell-preview {
  width: 95%;
  border-top-right-radius: 999px;
  border-bottom-right-radius: 999px;
}
[dir=rtl] .mat-calendar-body-range-end:not(.mat-calendar-body-in-comparison-range)::before,
[dir=rtl] .mat-calendar-body-range-end::after,
[dir=rtl] .mat-calendar-body-comparison-end:not(.mat-calendar-body-comparison-bridge-end)::before,
[dir=rtl] .mat-calendar-body-comparison-end::after,
[dir=rtl] .mat-calendar-body-preview-end .mat-calendar-body-cell-preview {
  left: 5%;
  border-radius: 0;
  border-top-left-radius: 999px;
  border-bottom-left-radius: 999px;
}

[dir=rtl] .mat-calendar-body-comparison-bridge-start.mat-calendar-body-range-end::after,
[dir=rtl] .mat-calendar-body-comparison-bridge-end.mat-calendar-body-range-start::after {
  width: 95%;
  border-top-right-radius: 999px;
  border-bottom-right-radius: 999px;
}

.mat-calendar-body-comparison-start.mat-calendar-body-range-end::after, [dir=rtl] .mat-calendar-body-comparison-start.mat-calendar-body-range-end::after,
.mat-calendar-body-comparison-end.mat-calendar-body-range-start::after,
[dir=rtl] .mat-calendar-body-comparison-end.mat-calendar-body-range-start::after {
  width: 90%;
}

.mat-calendar-body-in-preview {
  color: var(--mat-datepicker-calendar-date-preview-state-outline-color, var(--mat-sys-primary));
}
.mat-calendar-body-in-preview .mat-calendar-body-cell-preview {
  border-top: dashed 1px;
  border-bottom: dashed 1px;
}

.mat-calendar-body-preview-start .mat-calendar-body-cell-preview {
  border-left: dashed 1px;
}
[dir=rtl] .mat-calendar-body-preview-start .mat-calendar-body-cell-preview {
  border-left: 0;
  border-right: dashed 1px;
}

.mat-calendar-body-preview-end .mat-calendar-body-cell-preview {
  border-right: dashed 1px;
}
[dir=rtl] .mat-calendar-body-preview-end .mat-calendar-body-cell-preview {
  border-right: 0;
  border-left: dashed 1px;
}

.mat-calendar-body-disabled {
  cursor: default;
}
.mat-calendar-body-disabled > .mat-calendar-body-cell-content:not(.mat-calendar-body-selected):not(.mat-calendar-body-comparison-identical) {
  color: var(--mat-datepicker-calendar-date-disabled-state-text-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
}
.mat-calendar-body-disabled > .mat-calendar-body-today:not(.mat-calendar-body-selected):not(.mat-calendar-body-comparison-identical) {
  border-color: var(--mat-datepicker-calendar-date-today-disabled-state-outline-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
}
@media (forced-colors: active) {
  .mat-calendar-body-disabled {
    opacity: 0.5;
  }
}

.mat-calendar-body-cell-content {
  top: 5%;
  left: 5%;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  width: 90%;
  height: 90%;
  line-height: 1;
  border-width: 1px;
  border-style: solid;
  border-radius: 999px;
  color: var(--mat-datepicker-calendar-date-text-color, var(--mat-sys-on-surface));
  border-color: var(--mat-datepicker-calendar-date-outline-color, transparent);
}
.mat-calendar-body-cell-content.mat-focus-indicator {
  position: absolute;
}
@media (forced-colors: active) {
  .mat-calendar-body-cell-content {
    border: none;
  }
}

.cdk-keyboard-focused .mat-calendar-body-active > .mat-calendar-body-cell-content:not(.mat-calendar-body-selected):not(.mat-calendar-body-comparison-identical), .cdk-program-focused .mat-calendar-body-active > .mat-calendar-body-cell-content:not(.mat-calendar-body-selected):not(.mat-calendar-body-comparison-identical) {
  background-color: var(--mat-datepicker-calendar-date-focus-state-background-color, color-mix(in srgb, var(--mat-sys-on-surface) calc(var(--mat-sys-focus-state-layer-opacity) * 100%), transparent));
}

@media (hover: hover) {
  .mat-calendar-body-cell:not(.mat-calendar-body-disabled):hover > .mat-calendar-body-cell-content:not(.mat-calendar-body-selected):not(.mat-calendar-body-comparison-identical) {
    background-color: var(--mat-datepicker-calendar-date-hover-state-background-color, color-mix(in srgb, var(--mat-sys-on-surface) calc(var(--mat-sys-hover-state-layer-opacity) * 100%), transparent));
  }
}
.mat-calendar-body-selected {
  background-color: var(--mat-datepicker-calendar-date-selected-state-background-color, var(--mat-sys-primary));
  color: var(--mat-datepicker-calendar-date-selected-state-text-color, var(--mat-sys-on-primary));
}
.mat-calendar-body-disabled > .mat-calendar-body-selected {
  background-color: var(--mat-datepicker-calendar-date-selected-disabled-state-background-color, color-mix(in srgb, var(--mat-sys-on-surface) 38%, transparent));
}
.mat-calendar-body-selected.mat-calendar-body-today {
  box-shadow: inset 0 0 0 1px var(--mat-datepicker-calendar-date-today-selected-state-outline-color, var(--mat-sys-primary));
}

.mat-calendar-body-in-range::before {
  background: var(--mat-datepicker-calendar-date-in-range-state-background-color, var(--mat-sys-primary-container));
}

.mat-calendar-body-comparison-identical,
.mat-calendar-body-in-comparison-range::before {
  background: var(--mat-datepicker-calendar-date-in-comparison-range-state-background-color, var(--mat-sys-tertiary-container));
}

.mat-calendar-body-comparison-identical,
.mat-calendar-body-in-comparison-range::before {
  background: var(--mat-datepicker-calendar-date-in-comparison-range-state-background-color, var(--mat-sys-tertiary-container));
}

.mat-calendar-body-comparison-bridge-start::before,
[dir=rtl] .mat-calendar-body-comparison-bridge-end::before {
  background: linear-gradient(to right, var(--mat-datepicker-calendar-date-in-range-state-background-color, var(--mat-sys-primary-container)) 50%, var(--mat-datepicker-calendar-date-in-comparison-range-state-background-color, var(--mat-sys-tertiary-container)) 50%);
}

.mat-calendar-body-comparison-bridge-end::before,
[dir=rtl] .mat-calendar-body-comparison-bridge-start::before {
  background: linear-gradient(to left, var(--mat-datepicker-calendar-date-in-range-state-background-color, var(--mat-sys-primary-container)) 50%, var(--mat-datepicker-calendar-date-in-comparison-range-state-background-color, var(--mat-sys-tertiary-container)) 50%);
}

.mat-calendar-body-in-range > .mat-calendar-body-comparison-identical,
.mat-calendar-body-in-comparison-range.mat-calendar-body-in-range::after {
  background: var(--mat-datepicker-calendar-date-in-overlap-range-state-background-color, var(--mat-sys-secondary-container));
}

.mat-calendar-body-comparison-identical.mat-calendar-body-selected,
.mat-calendar-body-in-comparison-range > .mat-calendar-body-selected {
  background: var(--mat-datepicker-calendar-date-in-overlap-range-selected-state-background-color, var(--mat-sys-secondary));
}

@media (forced-colors: active) {
  .mat-datepicker-popup:not(:empty),
  .mat-calendar-body-cell:not(.mat-calendar-body-in-range) .mat-calendar-body-selected {
    outline: solid 1px;
  }
  .mat-calendar-body-today {
    outline: dotted 1px;
  }
  .mat-calendar-body-cell::before,
  .mat-calendar-body-cell::after,
  .mat-calendar-body-selected {
    background: none;
  }
  .mat-calendar-body-in-range::before,
  .mat-calendar-body-comparison-bridge-start::before,
  .mat-calendar-body-comparison-bridge-end::before {
    border-top: solid 1px;
    border-bottom: solid 1px;
  }
  .mat-calendar-body-range-start::before {
    border-left: solid 1px;
  }
  [dir=rtl] .mat-calendar-body-range-start::before {
    border-left: 0;
    border-right: solid 1px;
  }
  .mat-calendar-body-range-end::before {
    border-right: solid 1px;
  }
  [dir=rtl] .mat-calendar-body-range-end::before {
    border-right: 0;
    border-left: solid 1px;
  }
  .mat-calendar-body-in-comparison-range::before {
    border-top: dashed 1px;
    border-bottom: dashed 1px;
  }
  .mat-calendar-body-comparison-start::before {
    border-left: dashed 1px;
  }
  [dir=rtl] .mat-calendar-body-comparison-start::before {
    border-left: 0;
    border-right: dashed 1px;
  }
  .mat-calendar-body-comparison-end::before {
    border-right: dashed 1px;
  }
  [dir=rtl] .mat-calendar-body-comparison-end::before {
    border-right: 0;
    border-left: dashed 1px;
  }
}
`],encapsulation:2,changeDetection:0})}return n})();function Re(n){return n?.nodeName==="TD"}function Oe(n){let r;return Re(n)?r=n:Re(n.parentNode)?r=n.parentNode:Re(n.parentNode?.parentNode)&&(r=n.parentNode.parentNode),r?.getAttribute("data-mat-row")!=null?r:null}function Le(n,r,e){return e!==null&&r!==e&&n<e&&n===r}function Pe(n,r,e){return r!==null&&r!==e&&n>=r&&n===e}function Ne(n,r,e,t){return t&&r!==null&&e!==null&&r!==e&&n>=r&&n<=e}function ha(n){let r=n.changedTouches[0];return document.elementFromPoint(r.clientX,r.clientY)}var M=class{start;end;_disableStructuralEquivalency;constructor(r,e){this.start=r,this.end=e}},Ce=(()=>{class n{selection;_adapter;_selectionChanged=new j;selectionChanged=this._selectionChanged;constructor(e,t){this.selection=e,this._adapter=t,this.selection=e}updateSelection(e,t){let a=this.selection;this.selection=e,this._selectionChanged.next({selection:e,source:t,oldValue:a})}ngOnDestroy(){this._selectionChanged.complete()}_isValidDateInstance(e){return this._adapter.isDateInstance(e)&&this._adapter.isValid(e)}static \u0275fac=function(t){nt()};static \u0275prov=P({token:n,factory:n.\u0275fac})}return n})(),Qa=(()=>{class n extends Ce{constructor(e){super(null,e)}add(e){super.updateSelection(e,this)}isValid(){return this.selection!=null&&this._isValidDateInstance(this.selection)}isComplete(){return this.selection!=null}clone(){let e=new n(this._adapter);return e.updateSelection(this.selection,this),e}static \u0275fac=function(t){return new(t||n)(pe(v))};static \u0275prov=P({token:n,factory:n.\u0275fac})}return n})();var Ga={provide:Ce,useFactory:()=>p(Ce,{optional:!0,skipSelf:!0})||new Qa(p(v))};var Da=new ee("MAT_DATE_RANGE_SELECTION_STRATEGY");var Ye=7,Za=0,_a=(()=>{class n{_changeDetectorRef=p(W);_dateFormats=p(q,{optional:!0});_dateAdapter=p(v,{optional:!0});_dir=p(be,{optional:!0});_rangeStrategy=p(Da,{optional:!0});_rerenderSubscription=J.EMPTY;_selectionKeyPressed=!1;get activeDate(){return this._activeDate}set activeDate(e){let t=this._activeDate,a=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))||this._dateAdapter.today();this._activeDate=this._dateAdapter.clampDate(a,this.minDate,this.maxDate),this._hasSameMonthAndYear(t,this._activeDate)||this._init()}_activeDate;get selected(){return this._selected}set selected(e){e instanceof M?this._selected=e:this._selected=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e)),this._setRanges(this._selected)}_selected=null;get minDate(){return this._minDate}set minDate(e){this._minDate=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_minDate=null;get maxDate(){return this._maxDate}set maxDate(e){this._maxDate=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_maxDate=null;dateFilter;dateClass;comparisonStart=null;comparisonEnd=null;startDateAccessibleName=null;endDateAccessibleName=null;activeDrag=null;selectedChange=new m;_userSelection=new m;dragStarted=new m;dragEnded=new m;activeDateChange=new m;_matCalendarBody;_monthLabel=_("");_weeks=_([]);_firstWeekOffset=_(0);_rangeStart=_(null);_rangeEnd=_(null);_comparisonRangeStart=_(null);_comparisonRangeEnd=_(null);_previewStart=_(null);_previewEnd=_(null);_isRange=_(!1);_todayDate=_(null);_weekdays=_([]);constructor(){p(ne).load(De),this._activeDate=this._dateAdapter.today()}ngAfterContentInit(){this._rerenderSubscription=this._dateAdapter.localeChanges.pipe(le(null)).subscribe(()=>this._init())}ngOnChanges(e){let t=e.comparisonStart||e.comparisonEnd;t&&!t.firstChange&&this._setRanges(this.selected),e.activeDrag&&!this.activeDrag&&this._clearPreview()}ngOnDestroy(){this._rerenderSubscription.unsubscribe()}_dateSelected(e){let t=e.value,a=this._getDateFromDayOfMonth(t),i,o;this._selected instanceof M?(i=this._getDateInCurrentMonth(this._selected.start),o=this._getDateInCurrentMonth(this._selected.end)):i=o=this._getDateInCurrentMonth(this._selected),(i!==t||o!==t)&&this.selectedChange.emit(a),this._userSelection.emit({value:a,event:e.event}),this._clearPreview(),this._changeDetectorRef.markForCheck()}_updateActiveDate(e){let t=e.value,a=this._activeDate;this.activeDate=this._getDateFromDayOfMonth(t),this._dateAdapter.compareDate(a,this.activeDate)&&this.activeDateChange.emit(this._activeDate)}_handleCalendarBodyKeydown(e){let t=this._activeDate,a=this._isRtl();switch(e.keyCode){case 37:this.activeDate=this._dateAdapter.addCalendarDays(this._activeDate,a?1:-1);break;case 39:this.activeDate=this._dateAdapter.addCalendarDays(this._activeDate,a?-1:1);break;case 38:this.activeDate=this._dateAdapter.addCalendarDays(this._activeDate,-7);break;case 40:this.activeDate=this._dateAdapter.addCalendarDays(this._activeDate,7);break;case 36:this.activeDate=this._dateAdapter.addCalendarDays(this._activeDate,1-this._dateAdapter.getDate(this._activeDate));break;case 35:this.activeDate=this._dateAdapter.addCalendarDays(this._activeDate,this._dateAdapter.getNumDaysInMonth(this._activeDate)-this._dateAdapter.getDate(this._activeDate));break;case 33:this.activeDate=e.altKey?this._dateAdapter.addCalendarYears(this._activeDate,-1):this._dateAdapter.addCalendarMonths(this._activeDate,-1);break;case 34:this.activeDate=e.altKey?this._dateAdapter.addCalendarYears(this._activeDate,1):this._dateAdapter.addCalendarMonths(this._activeDate,1);break;case 13:case 32:this._selectionKeyPressed=!0,this._canSelect(this._activeDate)&&e.preventDefault();return;case 27:this._previewEnd()!=null&&!It(e)&&(this._clearPreview(),this.activeDrag?this.dragEnded.emit({value:null,event:e}):(this.selectedChange.emit(null),this._userSelection.emit({value:null,event:e})),e.preventDefault(),e.stopPropagation());return;default:return}this._dateAdapter.compareDate(t,this.activeDate)&&(this.activeDateChange.emit(this.activeDate),this._focusActiveCellAfterViewChecked()),e.preventDefault()}_handleCalendarBodyKeyup(e){(e.keyCode===32||e.keyCode===13)&&(this._selectionKeyPressed&&this._canSelect(this._activeDate)&&this._dateSelected({value:this._dateAdapter.getDate(this._activeDate),event:e}),this._selectionKeyPressed=!1)}_init(){this._setRanges(this.selected),this._todayDate.set(this._getCellCompareValue(this._dateAdapter.today())),this._monthLabel.set(this._dateFormats.display.monthLabel?this._dateAdapter.format(this.activeDate,this._dateFormats.display.monthLabel):this._dateAdapter.getMonthNames("short")[this._dateAdapter.getMonth(this.activeDate)].toLocaleUpperCase());let e=this._dateAdapter.createDate(this._dateAdapter.getYear(this.activeDate),this._dateAdapter.getMonth(this.activeDate),1);this._firstWeekOffset.set((Ye+this._dateAdapter.getDayOfWeek(e)-this._dateAdapter.getFirstDayOfWeek())%Ye),this._initWeekdays(),this._createWeekCells(),this._changeDetectorRef.markForCheck()}_focusActiveCell(e){this._matCalendarBody._focusActiveCell(e)}_focusActiveCellAfterViewChecked(){this._matCalendarBody._scheduleFocusActiveCellAfterViewChecked()}_previewChanged({event:e,value:t}){if(this._rangeStrategy){let a=t?t.rawValue:null,i=this._rangeStrategy.createPreview(a,this.selected,e);if(this._previewStart.set(this._getCellCompareValue(i.start)),this._previewEnd.set(this._getCellCompareValue(i.end)),this.activeDrag&&a){let o=this._rangeStrategy.createDrag?.(this.activeDrag.value,this.selected,a,e);o&&(this._previewStart.set(this._getCellCompareValue(o.start)),this._previewEnd.set(this._getCellCompareValue(o.end)))}}}_dragEnded(e){if(this.activeDrag)if(e.value){let t=this._rangeStrategy?.createDrag?.(this.activeDrag.value,this.selected,e.value,e.event);this.dragEnded.emit({value:t??null,event:e.event})}else this.dragEnded.emit({value:null,event:e.event})}_getDateFromDayOfMonth(e){return this._dateAdapter.createDate(this._dateAdapter.getYear(this.activeDate),this._dateAdapter.getMonth(this.activeDate),e)}_initWeekdays(){let e=this._dateAdapter.getFirstDayOfWeek(),t=this._dateAdapter.getDayOfWeekNames("narrow"),i=this._dateAdapter.getDayOfWeekNames("long").map((o,f)=>({long:o,narrow:t[f],id:Za++}));this._weekdays.set(i.slice(e).concat(i.slice(0,e)))}_createWeekCells(){let e=this._dateAdapter.getNumDaysInMonth(this.activeDate),t=this._dateAdapter.getDateNames(),a=[[]];for(let i=0,o=this._firstWeekOffset();i<e;i++,o++){o==Ye&&(a.push([]),o=0);let f=this._dateAdapter.createDate(this._dateAdapter.getYear(this.activeDate),this._dateAdapter.getMonth(this.activeDate),i+1),ka=this._shouldEnableDate(f),Sa=this._dateAdapter.format(f,this._dateFormats.display.dateA11yLabel),Ea=this.dateClass?this.dateClass(f,"month"):void 0;a[a.length-1].push(new se(i+1,t[i],Sa,ka,Ea,this._getCellCompareValue(f),f))}this._weeks.set(a)}_shouldEnableDate(e){return!!e&&(!this.minDate||this._dateAdapter.compareDate(e,this.minDate)>=0)&&(!this.maxDate||this._dateAdapter.compareDate(e,this.maxDate)<=0)&&(!this.dateFilter||this.dateFilter(e))}_getDateInCurrentMonth(e){return e&&this._hasSameMonthAndYear(e,this.activeDate)?this._dateAdapter.getDate(e):null}_hasSameMonthAndYear(e,t){return!!(e&&t&&this._dateAdapter.getMonth(e)==this._dateAdapter.getMonth(t)&&this._dateAdapter.getYear(e)==this._dateAdapter.getYear(t))}_getCellCompareValue(e){if(e){let t=this._dateAdapter.getYear(e),a=this._dateAdapter.getMonth(e),i=this._dateAdapter.getDate(e);return new Date(t,a,i).getTime()}return null}_isRtl(){return this._dir&&this._dir.value==="rtl"}_setRanges(e){e instanceof M?(this._rangeStart.set(this._getCellCompareValue(e.start)),this._rangeEnd.set(this._getCellCompareValue(e.end)),this._isRange.set(!0)):(this._rangeStart.set(this._getCellCompareValue(e)),this._rangeEnd.set(this._rangeStart()),this._isRange.set(!1)),this._comparisonRangeStart.set(this._getCellCompareValue(this.comparisonStart)),this._comparisonRangeEnd.set(this._getCellCompareValue(this.comparisonEnd))}_canSelect(e){return!this.dateFilter||this.dateFilter(e)}_clearPreview(){this._previewStart.set(null),this._previewEnd.set(null)}static \u0275fac=function(t){return new(t||n)};static \u0275cmp=k({type:n,selectors:[["mat-month-view"]],viewQuery:function(t,a){if(t&1&&K(Z,5),t&2){let i;T(i=F())&&(a._matCalendarBody=i.first)}},inputs:{activeDate:"activeDate",selected:"selected",minDate:"minDate",maxDate:"maxDate",dateFilter:"dateFilter",dateClass:"dateClass",comparisonStart:"comparisonStart",comparisonEnd:"comparisonEnd",startDateAccessibleName:"startDateAccessibleName",endDateAccessibleName:"endDateAccessibleName",activeDrag:"activeDrag"},outputs:{selectedChange:"selectedChange",_userSelection:"_userSelection",dragStarted:"dragStarted",dragEnded:"dragEnded",activeDateChange:"activeDateChange"},exportAs:["matMonthView"],features:[ae],decls:8,vars:14,consts:[["role","grid",1,"mat-calendar-table"],[1,"mat-calendar-table-header"],["scope","col"],["aria-hidden","true"],["colspan","7",1,"mat-calendar-table-header-divider"],["mat-calendar-body","",3,"selectedValueChange","activeDateChange","previewChange","dragStarted","dragEnded","keyup","keydown","label","rows","todayValue","startValue","endValue","comparisonStart","comparisonEnd","previewStart","previewEnd","isRange","labelMinRequiredCells","activeCell","startDateAccessibleName","endDateAccessibleName"],[1,"cdk-visually-hidden"]],template:function(t,a){t&1&&(s(0,"table",0)(1,"thead",1)(2,"tr"),he(3,La,5,2,"th",2,ba),d(),s(5,"tr",3),w(6,"th",4),d()(),s(7,"tbody",5),g("selectedValueChange",function(o){return a._dateSelected(o)})("activeDateChange",function(o){return a._updateActiveDate(o)})("previewChange",function(o){return a._previewChanged(o)})("dragStarted",function(o){return a.dragStarted.emit(o)})("dragEnded",function(o){return a._dragEnded(o)})("keyup",function(o){return a._handleCalendarBodyKeyup(o)})("keydown",function(o){return a._handleCalendarBodyKeydown(o)}),d()()),t&2&&(l(3),_e(a._weekdays()),l(4),h("label",a._monthLabel())("rows",a._weeks())("todayValue",a._todayDate())("startValue",a._rangeStart())("endValue",a._rangeEnd())("comparisonStart",a._comparisonRangeStart())("comparisonEnd",a._comparisonRangeEnd())("previewStart",a._previewStart())("previewEnd",a._previewEnd())("isRange",a._isRange())("labelMinRequiredCells",3)("activeCell",a._dateAdapter.getDate(a.activeDate)-1)("startDateAccessibleName",a.startDateAccessibleName)("endDateAccessibleName",a.endDateAccessibleName))},dependencies:[Z],encapsulation:2,changeDetection:0})}return n})(),C=24,Be=4,ga=(()=>{class n{_changeDetectorRef=p(W);_dateAdapter=p(v,{optional:!0});_dir=p(be,{optional:!0});_rerenderSubscription=J.EMPTY;_selectionKeyPressed=!1;get activeDate(){return this._activeDate}set activeDate(e){let t=this._activeDate,a=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))||this._dateAdapter.today();this._activeDate=this._dateAdapter.clampDate(a,this.minDate,this.maxDate),va(this._dateAdapter,t,this._activeDate,this.minDate,this.maxDate)||this._init()}_activeDate;get selected(){return this._selected}set selected(e){e instanceof M?this._selected=e:this._selected=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e)),this._setSelectedYear(e)}_selected=null;get minDate(){return this._minDate}set minDate(e){this._minDate=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_minDate=null;get maxDate(){return this._maxDate}set maxDate(e){this._maxDate=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_maxDate=null;dateFilter;dateClass;selectedChange=new m;yearSelected=new m;activeDateChange=new m;_matCalendarBody;_years=_([]);_todayYear=_(0);_selectedYear=_(null);constructor(){this._dateAdapter,this._activeDate=this._dateAdapter.today()}ngAfterContentInit(){this._rerenderSubscription=this._dateAdapter.localeChanges.pipe(le(null)).subscribe(()=>this._init())}ngOnDestroy(){this._rerenderSubscription.unsubscribe()}_init(){this._todayYear.set(this._dateAdapter.getYear(this._dateAdapter.today()));let t=this._dateAdapter.getYear(this._activeDate)-oe(this._dateAdapter,this.activeDate,this.minDate,this.maxDate),a=[];for(let i=0,o=[];i<C;i++)o.push(t+i),o.length==Be&&(a.push(o.map(f=>this._createCellForYear(f))),o=[]);this._years.set(a),this._changeDetectorRef.markForCheck()}_yearSelected(e){let t=e.value,a=this._dateAdapter.createDate(t,0,1),i=this._getDateFromYear(t);this.yearSelected.emit(a),this.selectedChange.emit(i)}_updateActiveDate(e){let t=e.value,a=this._activeDate;this.activeDate=this._getDateFromYear(t),this._dateAdapter.compareDate(a,this.activeDate)&&this.activeDateChange.emit(this.activeDate)}_handleCalendarBodyKeydown(e){let t=this._activeDate,a=this._isRtl();switch(e.keyCode){case 37:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,a?1:-1);break;case 39:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,a?-1:1);break;case 38:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,-Be);break;case 40:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,Be);break;case 36:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,-oe(this._dateAdapter,this.activeDate,this.minDate,this.maxDate));break;case 35:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,C-oe(this._dateAdapter,this.activeDate,this.minDate,this.maxDate)-1);break;case 33:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,e.altKey?-C*10:-C);break;case 34:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,e.altKey?C*10:C);break;case 13:case 32:this._selectionKeyPressed=!0;break;default:return}this._dateAdapter.compareDate(t,this.activeDate)&&this.activeDateChange.emit(this.activeDate),this._focusActiveCellAfterViewChecked(),e.preventDefault()}_handleCalendarBodyKeyup(e){(e.keyCode===32||e.keyCode===13)&&(this._selectionKeyPressed&&this._yearSelected({value:this._dateAdapter.getYear(this._activeDate),event:e}),this._selectionKeyPressed=!1)}_getActiveCell(){return oe(this._dateAdapter,this.activeDate,this.minDate,this.maxDate)}_focusActiveCell(){this._matCalendarBody._focusActiveCell()}_focusActiveCellAfterViewChecked(){this._matCalendarBody._scheduleFocusActiveCellAfterViewChecked()}_getDateFromYear(e){let t=this._dateAdapter.getMonth(this.activeDate),a=this._dateAdapter.getNumDaysInMonth(this._dateAdapter.createDate(e,t,1));return this._dateAdapter.createDate(e,t,Math.min(this._dateAdapter.getDate(this.activeDate),a))}_createCellForYear(e){let t=this._dateAdapter.createDate(e,0,1),a=this._dateAdapter.getYearName(t),i=this.dateClass?this.dateClass(t,"multi-year"):void 0;return new se(e,a,a,this._shouldEnableYear(e),i)}_shouldEnableYear(e){if(e==null||this.maxDate&&e>this._dateAdapter.getYear(this.maxDate)||this.minDate&&e<this._dateAdapter.getYear(this.minDate))return!1;if(!this.dateFilter)return!0;let t=this._dateAdapter.createDate(e,0,1);for(let a=t;this._dateAdapter.getYear(a)==e;a=this._dateAdapter.addCalendarDays(a,1))if(this.dateFilter(a))return!0;return!1}_isRtl(){return this._dir&&this._dir.value==="rtl"}_setSelectedYear(e){if(this._selectedYear.set(null),e instanceof M){let t=e.start||e.end;t&&this._selectedYear.set(this._dateAdapter.getYear(t))}else e&&this._selectedYear.set(this._dateAdapter.getYear(e))}static \u0275fac=function(t){return new(t||n)};static \u0275cmp=k({type:n,selectors:[["mat-multi-year-view"]],viewQuery:function(t,a){if(t&1&&K(Z,5),t&2){let i;T(i=F())&&(a._matCalendarBody=i.first)}},inputs:{activeDate:"activeDate",selected:"selected",minDate:"minDate",maxDate:"maxDate",dateFilter:"dateFilter",dateClass:"dateClass"},outputs:{selectedChange:"selectedChange",yearSelected:"yearSelected",activeDateChange:"activeDateChange"},exportAs:["matMultiYearView"],decls:5,vars:7,consts:[["role","grid",1,"mat-calendar-table"],["aria-hidden","true",1,"mat-calendar-table-header"],["colspan","4",1,"mat-calendar-table-header-divider"],["mat-calendar-body","",3,"selectedValueChange","activeDateChange","keyup","keydown","rows","todayValue","startValue","endValue","numCols","cellAspectRatio","activeCell"]],template:function(t,a){t&1&&(s(0,"table",0)(1,"thead",1)(2,"tr"),w(3,"th",2),d()(),s(4,"tbody",3),g("selectedValueChange",function(o){return a._yearSelected(o)})("activeDateChange",function(o){return a._updateActiveDate(o)})("keyup",function(o){return a._handleCalendarBodyKeyup(o)})("keydown",function(o){return a._handleCalendarBodyKeydown(o)}),d()()),t&2&&(l(4),h("rows",a._years())("todayValue",a._todayYear())("startValue",a._selectedYear())("endValue",a._selectedYear())("numCols",4)("cellAspectRatio",4/7)("activeCell",a._getActiveCell()))},dependencies:[Z],encapsulation:2,changeDetection:0})}return n})();function va(n,r,e,t,a){let i=n.getYear(r),o=n.getYear(e),f=ya(n,t,a);return Math.floor((i-f)/C)===Math.floor((o-f)/C)}function oe(n,r,e,t){let a=n.getYear(r);return Xa(a-ya(n,e,t),C)}function ya(n,r,e){let t=0;return e?t=n.getYear(e)-C+1:r&&(t=n.getYear(r)),t}function Xa(n,r){return(n%r+r)%r}var fa=(()=>{class n{_changeDetectorRef=p(W);_dateFormats=p(q,{optional:!0});_dateAdapter=p(v,{optional:!0});_dir=p(be,{optional:!0});_rerenderSubscription=J.EMPTY;_selectionKeyPressed=!1;get activeDate(){return this._activeDate}set activeDate(e){let t=this._activeDate,a=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))||this._dateAdapter.today();this._activeDate=this._dateAdapter.clampDate(a,this.minDate,this.maxDate),this._dateAdapter.getYear(t)!==this._dateAdapter.getYear(this._activeDate)&&this._init()}_activeDate;get selected(){return this._selected}set selected(e){e instanceof M?this._selected=e:this._selected=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e)),this._setSelectedMonth(e)}_selected=null;get minDate(){return this._minDate}set minDate(e){this._minDate=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_minDate=null;get maxDate(){return this._maxDate}set maxDate(e){this._maxDate=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_maxDate=null;dateFilter;dateClass;selectedChange=new m;monthSelected=new m;activeDateChange=new m;_matCalendarBody;_months=_([]);_yearLabel=_("");_todayMonth=_(null);_selectedMonth=_(null);constructor(){this._activeDate=this._dateAdapter.today()}ngAfterContentInit(){this._rerenderSubscription=this._dateAdapter.localeChanges.pipe(le(null)).subscribe(()=>this._init())}ngOnDestroy(){this._rerenderSubscription.unsubscribe()}_monthSelected(e){let t=e.value,a=this._dateAdapter.createDate(this._dateAdapter.getYear(this.activeDate),t,1);this.monthSelected.emit(a);let i=this._getDateFromMonth(t);this.selectedChange.emit(i)}_updateActiveDate(e){let t=e.value,a=this._activeDate;this.activeDate=this._getDateFromMonth(t),this._dateAdapter.compareDate(a,this.activeDate)&&this.activeDateChange.emit(this.activeDate)}_handleCalendarBodyKeydown(e){let t=this._activeDate,a=this._isRtl();switch(e.keyCode){case 37:this.activeDate=this._dateAdapter.addCalendarMonths(this._activeDate,a?1:-1);break;case 39:this.activeDate=this._dateAdapter.addCalendarMonths(this._activeDate,a?-1:1);break;case 38:this.activeDate=this._dateAdapter.addCalendarMonths(this._activeDate,-4);break;case 40:this.activeDate=this._dateAdapter.addCalendarMonths(this._activeDate,4);break;case 36:this.activeDate=this._dateAdapter.addCalendarMonths(this._activeDate,-this._dateAdapter.getMonth(this._activeDate));break;case 35:this.activeDate=this._dateAdapter.addCalendarMonths(this._activeDate,11-this._dateAdapter.getMonth(this._activeDate));break;case 33:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,e.altKey?-10:-1);break;case 34:this.activeDate=this._dateAdapter.addCalendarYears(this._activeDate,e.altKey?10:1);break;case 13:case 32:this._selectionKeyPressed=!0;break;default:return}this._dateAdapter.compareDate(t,this.activeDate)&&(this.activeDateChange.emit(this.activeDate),this._focusActiveCellAfterViewChecked()),e.preventDefault()}_handleCalendarBodyKeyup(e){(e.keyCode===32||e.keyCode===13)&&(this._selectionKeyPressed&&this._monthSelected({value:this._dateAdapter.getMonth(this._activeDate),event:e}),this._selectionKeyPressed=!1)}_init(){this._setSelectedMonth(this.selected),this._todayMonth.set(this._getMonthInCurrentYear(this._dateAdapter.today())),this._yearLabel.set(this._dateAdapter.getYearName(this.activeDate));let e=this._dateAdapter.getMonthNames("short");this._months.set([[0,1,2,3],[4,5,6,7],[8,9,10,11]].map(t=>t.map(a=>this._createCellForMonth(a,e[a])))),this._changeDetectorRef.markForCheck()}_focusActiveCell(){this._matCalendarBody._focusActiveCell()}_focusActiveCellAfterViewChecked(){this._matCalendarBody._scheduleFocusActiveCellAfterViewChecked()}_getMonthInCurrentYear(e){return e&&this._dateAdapter.getYear(e)==this._dateAdapter.getYear(this.activeDate)?this._dateAdapter.getMonth(e):null}_getDateFromMonth(e){let t=this._dateAdapter.createDate(this._dateAdapter.getYear(this.activeDate),e,1),a=this._dateAdapter.getNumDaysInMonth(t);return this._dateAdapter.createDate(this._dateAdapter.getYear(this.activeDate),e,Math.min(this._dateAdapter.getDate(this.activeDate),a))}_createCellForMonth(e,t){let a=this._dateAdapter.createDate(this._dateAdapter.getYear(this.activeDate),e,1),i=this._dateAdapter.format(a,this._dateFormats.display.monthYearA11yLabel),o=this.dateClass?this.dateClass(a,"year"):void 0;return new se(e,t.toLocaleUpperCase(),i,this._shouldEnableMonth(e),o)}_shouldEnableMonth(e){let t=this._dateAdapter.getYear(this.activeDate);if(e==null||this._isYearAndMonthAfterMaxDate(t,e)||this._isYearAndMonthBeforeMinDate(t,e))return!1;if(!this.dateFilter)return!0;let a=this._dateAdapter.createDate(t,e,1);for(let i=a;this._dateAdapter.getMonth(i)==e;i=this._dateAdapter.addCalendarDays(i,1))if(this.dateFilter(i))return!0;return!1}_isYearAndMonthAfterMaxDate(e,t){if(this.maxDate){let a=this._dateAdapter.getYear(this.maxDate),i=this._dateAdapter.getMonth(this.maxDate);return e>a||e===a&&t>i}return!1}_isYearAndMonthBeforeMinDate(e,t){if(this.minDate){let a=this._dateAdapter.getYear(this.minDate),i=this._dateAdapter.getMonth(this.minDate);return e<a||e===a&&t<i}return!1}_isRtl(){return this._dir&&this._dir.value==="rtl"}_setSelectedMonth(e){e instanceof M?this._selectedMonth.set(this._getMonthInCurrentYear(e.start)||this._getMonthInCurrentYear(e.end)):this._selectedMonth.set(this._getMonthInCurrentYear(e))}static \u0275fac=function(t){return new(t||n)};static \u0275cmp=k({type:n,selectors:[["mat-year-view"]],viewQuery:function(t,a){if(t&1&&K(Z,5),t&2){let i;T(i=F())&&(a._matCalendarBody=i.first)}},inputs:{activeDate:"activeDate",selected:"selected",minDate:"minDate",maxDate:"maxDate",dateFilter:"dateFilter",dateClass:"dateClass"},outputs:{selectedChange:"selectedChange",monthSelected:"monthSelected",activeDateChange:"activeDateChange"},exportAs:["matYearView"],decls:5,vars:9,consts:[["role","grid",1,"mat-calendar-table"],["aria-hidden","true",1,"mat-calendar-table-header"],["colspan","4",1,"mat-calendar-table-header-divider"],["mat-calendar-body","",3,"selectedValueChange","activeDateChange","keyup","keydown","label","rows","todayValue","startValue","endValue","labelMinRequiredCells","numCols","cellAspectRatio","activeCell"]],template:function(t,a){t&1&&(s(0,"table",0)(1,"thead",1)(2,"tr"),w(3,"th",2),d()(),s(4,"tbody",3),g("selectedValueChange",function(o){return a._monthSelected(o)})("activeDateChange",function(o){return a._updateActiveDate(o)})("keyup",function(o){return a._handleCalendarBodyKeyup(o)})("keydown",function(o){return a._handleCalendarBodyKeydown(o)}),d()()),t&2&&(l(4),h("label",a._yearLabel())("rows",a._months())("todayValue",a._todayMonth())("startValue",a._selectedMonth())("endValue",a._selectedMonth())("labelMinRequiredCells",2)("numCols",4)("cellAspectRatio",4/7)("activeCell",a._dateAdapter.getMonth(a.activeDate)))},dependencies:[Z],encapsulation:2,changeDetection:0})}return n})(),Ca=(()=>{class n{_intl=p(X);calendar=p(He);_dateAdapter=p(v,{optional:!0});_dateFormats=p(q,{optional:!0});_periodButtonText;_periodButtonDescription;_periodButtonLabel;_prevButtonLabel;_nextButtonLabel;constructor(){p(ne).load(De);let e=p(W);this._updateLabels(),this.calendar.stateChanges.subscribe(()=>{this._updateLabels(),e.markForCheck()})}get periodButtonText(){return this._periodButtonText}get periodButtonDescription(){return this._periodButtonDescription}get periodButtonLabel(){return this._periodButtonLabel}get prevButtonLabel(){return this._prevButtonLabel}get nextButtonLabel(){return this._nextButtonLabel}currentPeriodClicked(){this.calendar.currentView=this.calendar.currentView=="month"?"multi-year":"month"}previousClicked(){this.previousEnabled()&&(this.calendar.activeDate=this.calendar.currentView=="month"?this._dateAdapter.addCalendarMonths(this.calendar.activeDate,-1):this._dateAdapter.addCalendarYears(this.calendar.activeDate,this.calendar.currentView=="year"?-1:-C))}nextClicked(){this.nextEnabled()&&(this.calendar.activeDate=this.calendar.currentView=="month"?this._dateAdapter.addCalendarMonths(this.calendar.activeDate,1):this._dateAdapter.addCalendarYears(this.calendar.activeDate,this.calendar.currentView=="year"?1:C))}previousEnabled(){return this.calendar.minDate?!this.calendar.minDate||!this._isSameView(this.calendar.activeDate,this.calendar.minDate):!0}nextEnabled(){return!this.calendar.maxDate||!this._isSameView(this.calendar.activeDate,this.calendar.maxDate)}_updateLabels(){let e=this.calendar,t=this._intl,a=this._dateAdapter;e.currentView==="month"?(this._periodButtonText=a.format(e.activeDate,this._dateFormats.display.monthYearLabel).toLocaleUpperCase(),this._periodButtonDescription=a.format(e.activeDate,this._dateFormats.display.monthYearLabel).toLocaleUpperCase(),this._periodButtonLabel=t.switchToMultiYearViewLabel,this._prevButtonLabel=t.prevMonthLabel,this._nextButtonLabel=t.nextMonthLabel):e.currentView==="year"?(this._periodButtonText=a.getYearName(e.activeDate),this._periodButtonDescription=a.getYearName(e.activeDate),this._periodButtonLabel=t.switchToMonthViewLabel,this._prevButtonLabel=t.prevYearLabel,this._nextButtonLabel=t.nextYearLabel):(this._periodButtonText=t.formatYearRange(...this._formatMinAndMaxYearLabels()),this._periodButtonDescription=t.formatYearRangeLabel(...this._formatMinAndMaxYearLabels()),this._periodButtonLabel=t.switchToMonthViewLabel,this._prevButtonLabel=t.prevMultiYearLabel,this._nextButtonLabel=t.nextMultiYearLabel)}_isSameView(e,t){return this.calendar.currentView=="month"?this._dateAdapter.getYear(e)==this._dateAdapter.getYear(t)&&this._dateAdapter.getMonth(e)==this._dateAdapter.getMonth(t):this.calendar.currentView=="year"?this._dateAdapter.getYear(e)==this._dateAdapter.getYear(t):va(this._dateAdapter,e,t,this.calendar.minDate,this.calendar.maxDate)}_formatMinAndMaxYearLabels(){let t=this._dateAdapter.getYear(this.calendar.activeDate)-oe(this._dateAdapter,this.calendar.activeDate,this.calendar.minDate,this.calendar.maxDate),a=t+C-1,i=this._dateAdapter.getYearName(this._dateAdapter.createDate(t,0,1)),o=this._dateAdapter.getYearName(this._dateAdapter.createDate(a,0,1));return[i,o]}_periodButtonLabelId=p(Ie).getId("mat-calendar-period-label-");static \u0275fac=function(t){return new(t||n)};static \u0275cmp=k({type:n,selectors:[["mat-calendar-header"]],exportAs:["matCalendarHeader"],ngContentSelectors:Pa,decls:17,vars:13,consts:[[1,"mat-calendar-header"],[1,"mat-calendar-controls"],["aria-live","polite",1,"cdk-visually-hidden",3,"id"],["matButton","","type","button",1,"mat-calendar-period-button",3,"click"],["aria-hidden","true"],["viewBox","0 0 10 5","focusable","false","aria-hidden","true",1,"mat-calendar-arrow"],["points","0,0 5,5 10,0"],[1,"mat-calendar-spacer"],["matIconButton","","type","button","disabledInteractive","",1,"mat-calendar-previous-button",3,"click","disabled","matTooltip"],["viewBox","0 0 24 24","focusable","false","aria-hidden","true"],["d","M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"],["matIconButton","","type","button","disabledInteractive","",1,"mat-calendar-next-button",3,"click","disabled","matTooltip"],["d","M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"]],template:function(t,a){t&1&&(Ee(),s(0,"div",0)(1,"div",1)(2,"span",2),c(3),d(),s(4,"button",3),g("click",function(){return a.currentPeriodClicked()}),s(5,"span",4),c(6),d(),te(),s(7,"svg",5),w(8,"polygon",6),d()(),Ae(),w(9,"div",7),xe(10),s(11,"button",8),g("click",function(){return a.previousClicked()}),te(),s(12,"svg",9),w(13,"path",10),d()(),Ae(),s(14,"button",11),g("click",function(){return a.nextClicked()}),te(),s(15,"svg",9),w(16,"path",12),d()()()()),t&2&&(l(2),h("id",a._periodButtonLabelId),l(),A(a.periodButtonDescription),l(),S("aria-label",a.periodButtonLabel)("aria-describedby",a._periodButtonLabelId),l(2),A(a.periodButtonText),l(),Y("mat-calendar-invert",a.calendar.currentView!=="month"),l(4),h("disabled",!a.previousEnabled())("matTooltip",a.prevButtonLabel),S("aria-label",a.prevButtonLabel),l(3),h("disabled",!a.nextEnabled())("matTooltip",a.nextButtonLabel),S("aria-label",a.nextButtonLabel))},dependencies:[re,ie,ca],encapsulation:2,changeDetection:0})}return n})(),He=(()=>{class n{_dateAdapter=p(v,{optional:!0});_dateFormats=p(q,{optional:!0});_changeDetectorRef=p(W);_elementRef=p(ue);headerComponent;_calendarHeaderPortal;_intlChanges;_moveFocusOnNextTick=!1;get startAt(){return this._startAt}set startAt(e){this._startAt=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_startAt=null;startView="month";get selected(){return this._selected}set selected(e){e instanceof M?this._selected=e:this._selected=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_selected=null;get minDate(){return this._minDate}set minDate(e){this._minDate=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_minDate=null;get maxDate(){return this._maxDate}set maxDate(e){this._maxDate=this._dateAdapter.getValidDateOrNull(this._dateAdapter.deserialize(e))}_maxDate=null;dateFilter;dateClass;comparisonStart=null;comparisonEnd=null;startDateAccessibleName=null;endDateAccessibleName=null;selectedChange=new m;yearSelected=new m;monthSelected=new m;viewChanged=new m(!0);_userSelection=new m;_userDragDrop=new m;monthView;yearView;multiYearView;get activeDate(){return this._clampedActiveDate}set activeDate(e){this._clampedActiveDate=this._dateAdapter.clampDate(e,this.minDate,this.maxDate),this.stateChanges.next(),this._changeDetectorRef.markForCheck()}_clampedActiveDate;get currentView(){return this._currentView}set currentView(e){let t=this._currentView!==e?e:null;this._currentView=e,this._moveFocusOnNextTick=!0,this._changeDetectorRef.markForCheck(),t&&(this.stateChanges.next(),this.viewChanged.emit(t))}_currentView;_activeDrag=null;stateChanges=new j;constructor(){this._intlChanges=p(X).changes.subscribe(()=>{this._changeDetectorRef.markForCheck(),this.stateChanges.next()})}ngAfterContentInit(){this._calendarHeaderPortal=new Bt(this.headerComponent||Ca),this.activeDate=this.startAt||this._dateAdapter.today(),this._currentView=this.startView}ngAfterViewChecked(){this._moveFocusOnNextTick&&(this._moveFocusOnNextTick=!1,this.focusActiveCell())}ngOnDestroy(){this._intlChanges.unsubscribe(),this.stateChanges.complete()}ngOnChanges(e){let t=e.minDate&&!this._dateAdapter.sameDate(e.minDate.previousValue,e.minDate.currentValue)?e.minDate:void 0,a=e.maxDate&&!this._dateAdapter.sameDate(e.maxDate.previousValue,e.maxDate.currentValue)?e.maxDate:void 0,i=t||a||e.dateFilter;if(i&&!i.firstChange){let o=this._getCurrentViewComponent();o&&(this._elementRef.nativeElement.contains(kt())&&(this._moveFocusOnNextTick=!0),this._changeDetectorRef.detectChanges(),o._init())}this.stateChanges.next()}focusActiveCell(){this._getCurrentViewComponent()?._focusActiveCell(!1)}updateTodaysDate(){this._getCurrentViewComponent()?._init()}_dateSelected(e){let t=e.value;(this.selected instanceof M||t&&!this._dateAdapter.sameDate(t,this.selected))&&this.selectedChange.emit(t),this._userSelection.emit(e)}_yearSelectedInMultiYearView(e){this.yearSelected.emit(e)}_monthSelectedInYearView(e){this.monthSelected.emit(e)}_goToDateInView(e,t){this.activeDate=e,this.currentView=t}_dragStarted(e){this._activeDrag=e}_dragEnded(e){this._activeDrag&&(e.value&&this._userDragDrop.emit(e),this._activeDrag=null)}_getCurrentViewComponent(){return this.monthView||this.yearView||this.multiYearView}static \u0275fac=function(t){return new(t||n)};static \u0275cmp=k({type:n,selectors:[["mat-calendar"]],viewQuery:function(t,a){if(t&1&&K(_a,5)(fa,5)(ga,5),t&2){let i;T(i=F())&&(a.monthView=i.first),T(i=F())&&(a.yearView=i.first),T(i=F())&&(a.multiYearView=i.first)}},hostAttrs:[1,"mat-calendar"],inputs:{headerComponent:"headerComponent",startAt:"startAt",startView:"startView",selected:"selected",minDate:"minDate",maxDate:"maxDate",dateFilter:"dateFilter",dateClass:"dateClass",comparisonStart:"comparisonStart",comparisonEnd:"comparisonEnd",startDateAccessibleName:"startDateAccessibleName",endDateAccessibleName:"endDateAccessibleName"},outputs:{selectedChange:"selectedChange",yearSelected:"yearSelected",monthSelected:"monthSelected",viewChanged:"viewChanged",_userSelection:"_userSelection",_userDragDrop:"_userDragDrop"},exportAs:["matCalendar"],features:[dt([Ga]),ae],decls:5,vars:2,consts:[[3,"cdkPortalOutlet"],["cdkMonitorSubtreeFocus","","tabindex","-1",1,"mat-calendar-content"],[3,"activeDate","selected","dateFilter","maxDate","minDate","dateClass","comparisonStart","comparisonEnd","startDateAccessibleName","endDateAccessibleName","activeDrag"],[3,"activeDate","selected","dateFilter","maxDate","minDate","dateClass"],[3,"activeDateChange","_userSelection","dragStarted","dragEnded","activeDate","selected","dateFilter","maxDate","minDate","dateClass","comparisonStart","comparisonEnd","startDateAccessibleName","endDateAccessibleName","activeDrag"],[3,"activeDateChange","monthSelected","selectedChange","activeDate","selected","dateFilter","maxDate","minDate","dateClass"],[3,"activeDateChange","yearSelected","selectedChange","activeDate","selected","dateFilter","maxDate","minDate","dateClass"]],template:function(t,a){if(t&1&&(y(0,Na,0,0,"ng-template",0),s(1,"div",1),E(2,Ya,1,11,"mat-month-view",2)(3,Ba,1,6,"mat-year-view",3)(4,Ha,1,6,"mat-multi-year-view",3),d()),t&2){let i;h("cdkPortalOutlet",a._calendarHeaderPortal),l(2),x((i=a.currentView)==="month"?2:i==="year"?3:i==="multi-year"?4:-1)}},dependencies:[Te,Et,_a,fa,ga],styles:[`.mat-calendar {
  display: block;
  line-height: normal;
  font-family: var(--mat-datepicker-calendar-text-font, var(--mat-sys-body-medium-font));
  font-size: var(--mat-datepicker-calendar-text-size, var(--mat-sys-body-medium-size));
}

.mat-calendar-header {
  padding: 8px 8px 0 8px;
}

.mat-calendar-content {
  padding: 0 8px 8px 8px;
  outline: none;
}

.mat-calendar-controls {
  display: flex;
  align-items: center;
  margin: 5% calc(4.7142857143% - 16px);
}

.mat-calendar-spacer {
  flex: 1 1 auto;
}

.mat-calendar-period-button {
  min-width: 0;
  margin: 0 8px;
  font-size: var(--mat-datepicker-calendar-period-button-text-size, var(--mat-sys-title-small-size));
  font-weight: var(--mat-datepicker-calendar-period-button-text-weight, var(--mat-sys-title-small-weight));
  --mat-button-text-label-text-color: var(--mat-datepicker-calendar-period-button-text-color, var(--mat-sys-on-surface-variant));
}

.mat-calendar-arrow {
  display: inline-block;
  width: 10px;
  height: 5px;
  margin: 0 0 0 5px;
  vertical-align: middle;
  fill: var(--mat-datepicker-calendar-period-button-icon-color, var(--mat-sys-on-surface-variant));
}
.mat-calendar-arrow.mat-calendar-invert {
  transform: rotate(180deg);
}
[dir=rtl] .mat-calendar-arrow {
  margin: 0 5px 0 0;
}
@media (forced-colors: active) {
  .mat-calendar-arrow {
    fill: CanvasText;
  }
}

.mat-datepicker-content .mat-calendar-previous-button:not(.mat-mdc-button-disabled),
.mat-datepicker-content .mat-calendar-next-button:not(.mat-mdc-button-disabled) {
  color: var(--mat-datepicker-calendar-navigation-button-icon-color, var(--mat-sys-on-surface-variant));
}
[dir=rtl] .mat-calendar-previous-button,
[dir=rtl] .mat-calendar-next-button {
  transform: rotate(180deg);
}

.mat-calendar-table {
  border-spacing: 0;
  border-collapse: collapse;
  width: 100%;
}

.mat-calendar-table-header th {
  text-align: center;
  padding: 0 0 8px 0;
  color: var(--mat-datepicker-calendar-header-text-color, var(--mat-sys-on-surface-variant));
  font-size: var(--mat-datepicker-calendar-header-text-size, var(--mat-sys-title-small-size));
  font-weight: var(--mat-datepicker-calendar-header-text-weight, var(--mat-sys-title-small-weight));
}

.mat-calendar-table-header-divider {
  position: relative;
  height: 1px;
}
.mat-calendar-table-header-divider::after {
  content: "";
  position: absolute;
  top: 0;
  left: -8px;
  right: -8px;
  height: 1px;
  background: var(--mat-datepicker-calendar-header-divider-color, transparent);
}

.mat-calendar-body-cell-content::before {
  margin: calc(calc(var(--mat-focus-indicator-border-width, 3px) + 3px) * -1);
}

.mat-calendar-body-cell:focus-visible .mat-focus-indicator::before {
  content: "";
}
`],encapsulation:2,changeDetection:0})}return n})();var Ja=(()=>{class n{_elementRef=p(ue);_animationsDisabled=Tt();_changeDetectorRef=p(W);_globalModel=p(Ce);_dateAdapter=p(v);_ngZone=p(Me);_rangeSelectionStrategy=p(Da,{optional:!0});_stateChanges;_model;_eventCleanups;_animationFallback;_calendar;color;datepicker;comparisonStart=null;comparisonEnd=null;startDateAccessibleName=null;endDateAccessibleName=null;_isAbove=!1;_animationDone=new j;_isAnimating=!1;_closeButtonText;_closeButtonFocused=!1;_actionsPortal=null;_dialogLabelId=null;constructor(){if(p(ne).load(De),this._closeButtonText=p(X).closeCalendarLabel,!this._animationsDisabled){let e=this._elementRef.nativeElement,t=p(ke);this._eventCleanups=this._ngZone.runOutsideAngular(()=>[t.listen(e,"animationstart",this._handleAnimationEvent),t.listen(e,"animationend",this._handleAnimationEvent),t.listen(e,"animationcancel",this._handleAnimationEvent)])}}ngAfterViewInit(){this._stateChanges=this.datepicker.stateChanges.subscribe(()=>{this._changeDetectorRef.markForCheck()}),this._calendar.focusActiveCell()}ngOnDestroy(){clearTimeout(this._animationFallback),this._eventCleanups?.forEach(e=>e()),this._stateChanges?.unsubscribe(),this._animationDone.complete()}_handleUserSelection(e){let t=this._model.selection,a=e.value,i=t instanceof M;if(i&&this._rangeSelectionStrategy){let o=this._rangeSelectionStrategy.selectionFinished(a,t,e.event);this._model.updateSelection(o,this)}else a&&(i||!this._dateAdapter.sameDate(a,t))&&this._model.add(a);(!this._model||this._model.isComplete())&&!this._actionsPortal&&this.datepicker.close()}_handleUserDragDrop(e){this._model.updateSelection(e.value,this)}_startExitAnimation(){this._elementRef.nativeElement.classList.add("mat-datepicker-content-exit"),this._animationsDisabled?this._animationDone.next():(clearTimeout(this._animationFallback),this._animationFallback=setTimeout(()=>{this._isAnimating||this._animationDone.next()},200))}_handleAnimationEvent=e=>{let t=this._elementRef.nativeElement;e.target!==t||!e.animationName.startsWith("_mat-datepicker-content")||(clearTimeout(this._animationFallback),this._isAnimating=e.type==="animationstart",t.classList.toggle("mat-datepicker-content-animating",this._isAnimating),this._isAnimating||this._animationDone.next())};_getSelected(){return this._model.selection}_applyPendingSelection(){this._model!==this._globalModel&&this._globalModel.updateSelection(this._model.selection,this)}_assignActions(e,t){this._model=e?this._globalModel.clone():this._globalModel,this._actionsPortal=e,t&&this._changeDetectorRef.detectChanges()}static \u0275fac=function(t){return new(t||n)};static \u0275cmp=k({type:n,selectors:[["mat-datepicker-content"]],viewQuery:function(t,a){if(t&1&&K(He,5),t&2){let i;T(i=F())&&(a._calendar=i.first)}},hostAttrs:[1,"mat-datepicker-content"],hostVars:6,hostBindings:function(t,a){t&2&&(fe(a.color?"mat-"+a.color:""),Y("mat-datepicker-content-touch",a.datepicker.touchUi)("mat-datepicker-content-animations-enabled",!a._animationsDisabled))},inputs:{color:"color"},exportAs:["matDatepickerContent"],decls:5,vars:26,consts:[["cdkTrapFocus","","role","dialog",1,"mat-datepicker-content-container"],[3,"yearSelected","monthSelected","viewChanged","_userSelection","_userDragDrop","id","startAt","startView","minDate","maxDate","dateFilter","headerComponent","selected","dateClass","comparisonStart","comparisonEnd","startDateAccessibleName","endDateAccessibleName"],[3,"cdkPortalOutlet"],["type","button","matButton","elevated",1,"mat-datepicker-close-button",3,"focus","blur","click","color"]],template:function(t,a){t&1&&(s(0,"div",0)(1,"mat-calendar",1),g("yearSelected",function(o){return a.datepicker._selectYear(o)})("monthSelected",function(o){return a.datepicker._selectMonth(o)})("viewChanged",function(o){return a.datepicker._viewChanged(o)})("_userSelection",function(o){return a._handleUserSelection(o)})("_userDragDrop",function(o){return a._handleUserDragDrop(o)}),d(),y(2,za,0,0,"ng-template",2),s(3,"button",3),g("focus",function(){return a._closeButtonFocused=!0})("blur",function(){return a._closeButtonFocused=!1})("click",function(){return a.datepicker.close()}),c(4),d()()),t&2&&(Y("mat-datepicker-content-container-with-custom-header",a.datepicker.calendarHeaderComponent)("mat-datepicker-content-container-with-actions",a._actionsPortal),S("aria-modal",!0)("aria-labelledby",a._dialogLabelId??void 0),l(),fe(a.datepicker.panelClass),h("id",a.datepicker.id)("startAt",a.datepicker.startAt)("startView",a.datepicker.startView)("minDate",a.datepicker._getMinDate())("maxDate",a.datepicker._getMaxDate())("dateFilter",a.datepicker._getDateFilter())("headerComponent",a.datepicker.calendarHeaderComponent)("selected",a._getSelected())("dateClass",a.datepicker.dateClass)("comparisonStart",a.comparisonStart)("comparisonEnd",a.comparisonEnd)("startDateAccessibleName",a.startDateAccessibleName)("endDateAccessibleName",a.endDateAccessibleName),l(),h("cdkPortalOutlet",a._actionsPortal),l(),Y("cdk-visually-hidden",!a._closeButtonFocused),h("color",a.color||"primary"),l(),A(a._closeButtonText))},dependencies:[xt,He,Te,re],styles:[`@keyframes _mat-datepicker-content-dropdown-enter {
  from {
    opacity: 0;
    transform: scaleY(0.8);
  }
  to {
    opacity: 1;
    transform: none;
  }
}
@keyframes _mat-datepicker-content-dialog-enter {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: none;
  }
}
@keyframes _mat-datepicker-content-exit {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}
.mat-datepicker-content {
  display: block;
  background-color: var(--mat-datepicker-calendar-container-background-color, var(--mat-sys-surface-container-high));
  color: var(--mat-datepicker-calendar-container-text-color, var(--mat-sys-on-surface));
  box-shadow: var(--mat-datepicker-calendar-container-elevation-shadow, 0px 0px 0px 0px rgba(0, 0, 0, 0.2), 0px 0px 0px 0px rgba(0, 0, 0, 0.14), 0px 0px 0px 0px rgba(0, 0, 0, 0.12));
  border-radius: var(--mat-datepicker-calendar-container-shape, var(--mat-sys-corner-large));
}
.mat-datepicker-content.mat-datepicker-content-animations-enabled {
  animation: _mat-datepicker-content-dropdown-enter 120ms cubic-bezier(0, 0, 0.2, 1);
}
.mat-datepicker-content .mat-calendar {
  width: 296px;
  height: 354px;
}
.mat-datepicker-content .mat-datepicker-content-container-with-custom-header .mat-calendar {
  height: auto;
}
.mat-datepicker-content .mat-datepicker-close-button {
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 8px;
}
.mat-datepicker-content-animating .mat-datepicker-content .mat-datepicker-close-button {
  display: none;
}

.mat-datepicker-content-container {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.mat-datepicker-content-touch {
  display: block;
  max-height: 80vh;
  box-shadow: var(--mat-datepicker-calendar-container-touch-elevation-shadow, 0px 0px 0px 0px rgba(0, 0, 0, 0.2), 0px 0px 0px 0px rgba(0, 0, 0, 0.14), 0px 0px 0px 0px rgba(0, 0, 0, 0.12));
  border-radius: var(--mat-datepicker-calendar-container-touch-shape, var(--mat-sys-corner-extra-large));
  position: relative;
  overflow: visible;
}
.mat-datepicker-content-touch.mat-datepicker-content-animations-enabled {
  animation: _mat-datepicker-content-dialog-enter 150ms cubic-bezier(0, 0, 0.2, 1);
}
.mat-datepicker-content-touch .mat-datepicker-content-container {
  min-height: 312px;
  max-height: 788px;
  min-width: 250px;
  max-width: 750px;
}
.mat-datepicker-content-touch .mat-calendar {
  width: 100%;
  height: auto;
}

.mat-datepicker-content-exit.mat-datepicker-content-animations-enabled {
  animation: _mat-datepicker-content-exit 100ms linear;
}

@media all and (orientation: landscape) {
  .mat-datepicker-content-touch .mat-datepicker-content-container {
    width: 64vh;
    height: 80vh;
  }
}
@media all and (orientation: portrait) {
  .mat-datepicker-content-touch .mat-datepicker-content-container {
    width: 80vw;
    height: 100vw;
  }
  .mat-datepicker-content-touch .mat-datepicker-content-container-with-actions {
    height: 115vw;
  }
}
`],encapsulation:2,changeDetection:0})}return n})();var en=(()=>{class n{static \u0275fac=function(t){return new(t||n)};static \u0275dir=it({type:n,selectors:[["","matDatepickerToggleIcon",""]]})}return n})(),tn=(()=>{class n{_intl=p(X);_changeDetectorRef=p(W);_stateChanges=J.EMPTY;datepicker;tabIndex=null;ariaLabel;get disabled(){return this._disabled===void 0&&this.datepicker?this.datepicker.disabled:!!this._disabled}set disabled(e){this._disabled=e}_disabled;disableRipple=!1;_customIcon;_button;constructor(){let e=p(new mt("tabindex"),{optional:!0}),t=Number(e);this.tabIndex=t||t===0?t:null}ngOnChanges(e){e.datepicker&&this._watchStateChanges()}ngOnDestroy(){this._stateChanges.unsubscribe()}ngAfterContentInit(){this._watchStateChanges()}_open(e){this.datepicker&&!this.disabled&&(this.datepicker.open(),e.stopPropagation())}_watchStateChanges(){let e=this.datepicker?this.datepicker.stateChanges:de(),t=this.datepicker&&this.datepicker.datepickerInput?this.datepicker.datepickerInput.stateChanges:de(),a=this.datepicker?we(this.datepicker.openedStream,this.datepicker.closedStream):de();this._stateChanges.unsubscribe(),this._stateChanges=we(this._intl.changes,e,t,a).subscribe(()=>this._changeDetectorRef.markForCheck())}static \u0275fac=function(t){return new(t||n)};static \u0275cmp=k({type:n,selectors:[["mat-datepicker-toggle"]],contentQueries:function(t,a,i){if(t&1&&st(i,en,5),t&2){let o;T(o=F())&&(a._customIcon=o.first)}},viewQuery:function(t,a){if(t&1&&K(Wa,5),t&2){let i;T(i=F())&&(a._button=i.first)}},hostAttrs:[1,"mat-datepicker-toggle"],hostVars:8,hostBindings:function(t,a){t&1&&g("click",function(o){return a._open(o)}),t&2&&(S("tabindex",null)("data-mat-calendar",a.datepicker?a.datepicker.id:null),Y("mat-datepicker-toggle-active",a.datepicker&&a.datepicker.opened)("mat-accent",a.datepicker&&a.datepicker.color==="accent")("mat-warn",a.datepicker&&a.datepicker.color==="warn"))},inputs:{datepicker:[0,"for","datepicker"],tabIndex:"tabIndex",ariaLabel:[0,"aria-label","ariaLabel"],disabled:[2,"disabled","disabled",ht],disableRipple:"disableRipple"},exportAs:["matDatepickerToggle"],features:[ae],ngContentSelectors:Ka,decls:4,vars:7,consts:[["button",""],["matIconButton","","type","button",3,"tabIndex","disabled","disableRipple"],["viewBox","0 0 24 24","width","24px","height","24px","fill","currentColor","focusable","false","aria-hidden","true",1,"mat-datepicker-toggle-default-icon"],["d","M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z"]],template:function(t,a){t&1&&(Ee(ja),s(0,"button",1,0),E(2,qa,2,0,":svg:svg",2),xe(3),d()),t&2&&(h("tabIndex",a.disabled?-1:a.tabIndex)("disabled",a.disabled)("disableRipple",a.disableRipple),S("aria-haspopup",a.datepicker?"dialog":null)("aria-label",a.ariaLabel||a._intl.openCalendarLabel)("aria-expanded",a.datepicker?a.datepicker.opened:null),l(2),x(a._customIcon?-1:2))},dependencies:[ie],styles:[`.mat-datepicker-toggle {
  pointer-events: auto;
  color: var(--mat-datepicker-toggle-icon-color, var(--mat-sys-on-surface-variant));
}
.mat-datepicker-toggle button {
  color: inherit;
}

.mat-datepicker-toggle-active {
  color: var(--mat-datepicker-toggle-active-state-icon-color, var(--mat-sys-primary));
}

@media (forced-colors: active) {
  .mat-datepicker-toggle-default-icon {
    color: CanvasText;
  }
}
`],encapsulation:2,changeDetection:0})}return n})();var wa=(()=>{class n{static \u0275fac=function(t){return new(t||n)};static \u0275mod=me({type:n});static \u0275inj=ce({providers:[X],imports:[ve,oa,Vt,Ht,Ja,tn,Ca,St,Lt]})}return n})();var an=/^\d{4}-\d{2}-\d{2}(?:T\d{2}:\d{2}:\d{2}(?:\.\d+)?(?:Z|(?:(?:\+|-)\d{2}:\d{2}))?)?$/,nn=/^(\d?\d)[:.](\d?\d)(?:[:.](\d?\d))?\s*(AM|PM)?$/i;function Ze(n,r){let e=Array(n);for(let t=0;t<n;t++)e[t]=r(t);return e}var rn=(()=>{class n extends v{_matDateLocale=p(Fe,{optional:!0});constructor(){super();let e=p(Fe,{optional:!0});e!==void 0&&(this._matDateLocale=e),super.setLocale(this._matDateLocale)}getYear(e){return e.getFullYear()}getMonth(e){return e.getMonth()}getDate(e){return e.getDate()}getDayOfWeek(e){return e.getDay()}getMonthNames(e){let t=new Intl.DateTimeFormat(this.locale,{month:e,timeZone:"utc"});return Ze(12,a=>this._format(t,new Date(2017,a,1)))}getDateNames(){let e=new Intl.DateTimeFormat(this.locale,{day:"numeric",timeZone:"utc"});return Ze(31,t=>this._format(e,new Date(2017,0,t+1)))}getDayOfWeekNames(e){let t=new Intl.DateTimeFormat(this.locale,{weekday:e,timeZone:"utc"});return Ze(7,a=>this._format(t,new Date(2017,0,a+1)))}getYearName(e){let t=new Intl.DateTimeFormat(this.locale,{year:"numeric",timeZone:"utc"});return this._format(t,e)}getFirstDayOfWeek(){if(typeof Intl<"u"&&Intl.Locale){let e=new Intl.Locale(this.locale),t=(e.getWeekInfo?.()||e.weekInfo)?.firstDay??0;return t===7?0:t}return 0}getNumDaysInMonth(e){return this.getDate(this._createDateWithOverflow(this.getYear(e),this.getMonth(e)+1,0))}clone(e){return new Date(e.getTime())}createDate(e,t,a){let i=this._createDateWithOverflow(e,t,a);return i.getMonth()!=t,i}today(){return new Date}parse(e,t){return typeof e=="number"?new Date(e):e?new Date(Date.parse(e)):null}format(e,t){if(!this.isValid(e))throw Error("NativeDateAdapter: Cannot format invalid date.");let a=new Intl.DateTimeFormat(this.locale,et(Je({},t),{timeZone:"utc"}));return this._format(a,e)}addCalendarYears(e,t){return this.addCalendarMonths(e,t*12)}addCalendarMonths(e,t){let a=this._createDateWithOverflow(this.getYear(e),this.getMonth(e)+t,this.getDate(e));return this.getMonth(a)!=((this.getMonth(e)+t)%12+12)%12&&(a=this._createDateWithOverflow(this.getYear(a),this.getMonth(a),0)),a}addCalendarDays(e,t){return this._createDateWithOverflow(this.getYear(e),this.getMonth(e),this.getDate(e)+t)}toIso8601(e){return[e.getUTCFullYear(),this._2digit(e.getUTCMonth()+1),this._2digit(e.getUTCDate())].join("-")}deserialize(e){if(typeof e=="string"){if(!e)return null;if(an.test(e)){let t=new Date(e);if(this.isValid(t))return t}}return super.deserialize(e)}isDateInstance(e){return e instanceof Date}isValid(e){return!isNaN(e.getTime())}invalid(){return new Date(NaN)}setTime(e,t,a,i){let o=this.clone(e);return o.setHours(t,a,i,0),o}getHours(e){return e.getHours()}getMinutes(e){return e.getMinutes()}getSeconds(e){return e.getSeconds()}parseTime(e,t){if(typeof e!="string")return e instanceof Date?new Date(e.getTime()):null;let a=e.trim();if(a.length===0)return null;let i=this._parseTimeString(a);if(i===null){let o=a.replace(/[^0-9:(AM|PM)]/gi,"").trim();o.length>0&&(i=this._parseTimeString(o))}return i||this.invalid()}addSeconds(e,t){return new Date(e.getTime()+t*1e3)}_createDateWithOverflow(e,t,a){let i=new Date;return i.setFullYear(e,t,a),i.setHours(0,0,0,0),i}_2digit(e){return("00"+e).slice(-2)}_format(e,t){let a=new Date;return a.setUTCFullYear(t.getFullYear(),t.getMonth(),t.getDate()),a.setUTCHours(t.getHours(),t.getMinutes(),t.getSeconds(),t.getMilliseconds()),e.format(a)}_parseTimeString(e){let t=e.toUpperCase().match(nn);if(t){let a=parseInt(t[1]),i=parseInt(t[2]),o=t[3]==null?void 0:parseInt(t[3]),f=t[4];if(a===12?a=f==="AM"?0:a:f==="PM"&&(a+=12),Xe(a,0,23)&&Xe(i,0,59)&&(o==null||Xe(o,0,59)))return this.setTime(this.today(),a,i,o||0)}return null}static \u0275fac=function(t){return new(t||n)};static \u0275prov=P({token:n,factory:n.\u0275fac})}return n})();function Xe(n,r,e){return!isNaN(n)&&n>=r&&n<=e}var on={parse:{dateInput:null,timeInput:null},display:{dateInput:{year:"numeric",month:"numeric",day:"numeric"},timeInput:{hour:"numeric",minute:"numeric"},monthYearLabel:{year:"numeric",month:"short"},dateA11yLabel:{year:"numeric",month:"long",day:"numeric"},monthYearA11yLabel:{year:"numeric",month:"long"},timeOptionLabel:{hour:"numeric",minute:"numeric"}}};var Aa=(()=>{class n{static \u0275fac=function(t){return new(t||n)};static \u0275mod=me({type:n});static \u0275inj=ce({providers:[sn()]})}return n})();function sn(n=on){return[{provide:v,useClass:rn},{provide:q,useValue:n}]}var Ma=(()=>{class n{http;apiBase=At.apiUrl||"";apiUrl=`${this.apiBase}/api/audit-logs`;constructor(e){this.http=e}list(e){let t=new ft().set("page",e.page.toString()).set("size",e.size.toString());return e.action&&(t=t.set("action",e.action)),e.entityType&&(t=t.set("entityType",e.entityType)),e.status&&(t=t.set("status",e.status)),this.http.get(this.apiUrl,{params:t})}static \u0275fac=function(t){return new(t||n)(pe(bt))};static \u0275prov=P({token:n,factory:n.\u0275fac,providedIn:"root"})}return n})();var ln=()=>[5,10,20,50];function cn(n,r){if(n&1&&(s(0,"div",19)(1,"strong"),c(2,"Falha ao carregar auditoria."),d(),s(3,"p"),c(4),d()()),n&2){let e=u();l(4),A(e.error)}}function pn(n,r){n&1&&(s(0,"div",20)(1,"p"),c(2,"Carregando logs..."),d()())}function un(n,r){n&1&&(s(0,"div",21)(1,"strong"),c(2,"Nenhum log encontrado."),d(),s(3,"p"),c(4,"Ajuste os filtros ou gere novas operacoes no sistema para visualizar eventos."),d()())}function mn(n,r){n&1&&(s(0,"th",36),c(1,"ID"),d())}function hn(n,r){if(n&1&&(s(0,"td",37),c(1),d()),n&2){let e=r.$implicit;l(),A(e.id)}}function _n(n,r){n&1&&(s(0,"th",36),c(1,"Usu\xE1rio"),d())}function gn(n,r){if(n&1&&(s(0,"td",37),c(1),d()),n&2){let e=r.$implicit;l(),A(e.username)}}function fn(n,r){n&1&&(s(0,"th",36),c(1,"A\xE7\xE3o"),d())}function bn(n,r){if(n&1&&(s(0,"td",37)(1,"mat-chip",38),c(2),d()()),n&2){let e=r.$implicit,t=u(2);l(),h("color",t.getActionColor(e.action)),l(),R(" ",e.action," ")}}function Dn(n,r){n&1&&(s(0,"th",36),c(1,"Tipo"),d())}function vn(n,r){if(n&1&&(s(0,"td",37),c(1),d()),n&2){let e=r.$implicit;l(),A(e.entityType)}}function yn(n,r){n&1&&(s(0,"th",36),c(1,"Status"),d())}function Cn(n,r){if(n&1&&(s(0,"td",37)(1,"mat-chip",38),c(2),d()()),n&2){let e=r.$implicit,t=u(2);l(),h("color",t.getStatusColor(e.status)),l(),R(" ",e.status," ")}}function wn(n,r){n&1&&(s(0,"th",36),c(1,"IP"),d())}function An(n,r){if(n&1&&(s(0,"td",37),c(1),d()),n&2){let e=r.$implicit;l(),A(e.ipAddress)}}function Mn(n,r){n&1&&(s(0,"th",36),c(1,"Data"),d())}function kn(n,r){if(n&1&&(s(0,"td",37),c(1),ct(2,"date"),d()),n&2){let e=r.$implicit;l(),R(" ",pt(2,1,e.createdAt,"dd/MM/yyyy HH:mm:ss")," ")}}function Sn(n,r){n&1&&(s(0,"th",36),c(1,"A\xE7\xF5es"),d())}function En(n,r){if(n&1){let e=N();s(0,"td",37)(1,"button",39),g("click",function(){let a=b(e).$implicit,i=u(2);return D(i.viewDetails(a))}),s(2,"mat-icon"),c(3,"visibility"),d()()()}}function xn(n,r){n&1&&w(0,"tr",40)}function Vn(n,r){n&1&&w(0,"tr",41)}function In(n,r){if(n&1&&(s(0,"table",22),O(1,24),y(2,mn,2,0,"th",25)(3,hn,2,1,"td",26),L(),O(4,27),y(5,_n,2,0,"th",25)(6,gn,2,1,"td",26),L(),O(7,28),y(8,fn,2,0,"th",25)(9,bn,3,2,"td",26),L(),O(10,29),y(11,Dn,2,0,"th",25)(12,vn,2,1,"td",26),L(),O(13,30),y(14,yn,2,0,"th",25)(15,Cn,3,2,"td",26),L(),O(16,31),y(17,wn,2,0,"th",25)(18,An,2,1,"td",26),L(),O(19,32),y(20,Mn,2,0,"th",25)(21,kn,3,4,"td",26),L(),O(22,33),y(23,Sn,2,0,"th",25)(24,En,4,0,"td",26),L(),y(25,xn,1,0,"tr",34)(26,Vn,1,0,"tr",35),d()),n&2){let e=u();h("dataSource",e.auditLogs),l(25),h("matHeaderRowDef",e.displayedColumns),l(),h("matRowDefColumns",e.displayedColumns)}}function Tn(n,r){if(n&1){let e=N();s(0,"mat-paginator",42),g("page",function(a){b(e);let i=u();return D(i.onPageChange(a))}),d()}if(n&2){let e=u();h("pageSizeOptions",lt(3,ln))("pageSize",e.pageSize)("length",e.totalElements)}}var cr=(()=>{class n{route;auditLogService;displayedColumns=["id","username","action","entityType","status","ipAddress","createdAt","actions"];auditLogs=[];totalElements=0;pageSize=10;currentPage=0;loading=!1;error="";filterAction="";filterEntityType="";filterStatus="";constructor(e,t){this.route=e,this.auditLogService=t}ngOnInit(){this.loadAuditLogs()}loadAuditLogs(){this.loading=!0,this.error="",this.auditLogService.list({action:this.filterAction,entityType:this.filterEntityType,status:this.filterStatus,page:this.currentPage,size:this.pageSize}).subscribe({next:e=>{this.auditLogs=e.content??[],this.totalElements=e.totalElements??0,this.loading=!1},error:e=>{this.auditLogs=[],this.totalElements=0,this.error=na(e,"Nao foi possivel carregar os logs de auditoria."),this.loading=!1}})}onPageChange(e){this.currentPage=e.pageIndex,this.pageSize=e.pageSize,this.loadAuditLogs()}viewDetails(e){let t=this.parseJsonSafely(e.oldValue),a=this.parseJsonSafely(e.newValue);console.log("Old Value:",t),console.log("New Value:",a)}getActionColor(e){return{CREATE:"accent",UPDATE:"warn",DELETE:"primary",VIEW:"primary"}[e]||"primary"}getStatusColor(e){return e==="SUCCESS"?"accent":"warn"}parseJsonSafely(e){if(!e)return null;try{return JSON.parse(e)}catch(t){return e}}static \u0275fac=function(t){return new(t||n)(Se(Ct),Se(Ma))};static \u0275cmp=k({type:n,selectors:[["app-audit-logs"]],decls:56,vars:8,consts:[[1,"container"],[1,"header"],["mat-raised-button","","color","primary"],[1,"filters-card"],[1,"filters-grid"],["appearance","outline"],[3,"ngModelChange","change","ngModel"],["value",""],["value","CREATE"],["value","UPDATE"],["value","DELETE"],["value","VIEW"],["value","BOT"],["value","CONVERSATION"],["value","MESSAGE"],["value","USER"],["value","SUCCESS"],["value","FAILED"],[1,"logs-card"],[1,"state-card","state-card--error"],[1,"loading-state"],[1,"state-card","state-card--empty"],["mat-table","",1,"full-width-table",3,"dataSource"],[3,"pageSizeOptions","pageSize","length"],["matColumnDef","id"],["mat-header-cell","",4,"matHeaderCellDef"],["mat-cell","",4,"matCellDef"],["matColumnDef","username"],["matColumnDef","action"],["matColumnDef","entityType"],["matColumnDef","status"],["matColumnDef","ipAddress"],["matColumnDef","createdAt"],["matColumnDef","actions"],["mat-header-row","",4,"matHeaderRowDef"],["mat-row","",4,"matRowDef","matRowDefColumns"],["mat-header-cell",""],["mat-cell",""],["selected","",3,"color"],["mat-icon-button","","color","primary",3,"click"],["mat-header-row",""],["mat-row",""],[3,"page","pageSizeOptions","pageSize","length"]],template:function(t,a){t&1&&(s(0,"div",0)(1,"div",1)(2,"h1"),c(3,"Logs de Auditoria"),d(),s(4,"button",2)(5,"mat-icon"),c(6,"download"),d(),c(7," Exportar "),d()(),s(8,"mat-card",3)(9,"mat-card-content")(10,"div",4)(11,"mat-form-field",5)(12,"mat-label"),c(13,"A\xE7\xE3o"),d(),s(14,"mat-select",6),z("ngModelChange",function(o){return H(a.filterAction,o)||(a.filterAction=o),o}),g("change",function(){return a.loadAuditLogs()}),s(15,"mat-option",7),c(16,"Todas"),d(),s(17,"mat-option",8),c(18,"Criar"),d(),s(19,"mat-option",9),c(20,"Atualizar"),d(),s(21,"mat-option",10),c(22,"Deletar"),d(),s(23,"mat-option",11),c(24,"Visualizar"),d()()(),s(25,"mat-form-field",5)(26,"mat-label"),c(27,"Tipo de Entidade"),d(),s(28,"mat-select",6),z("ngModelChange",function(o){return H(a.filterEntityType,o)||(a.filterEntityType=o),o}),g("change",function(){return a.loadAuditLogs()}),s(29,"mat-option",7),c(30,"Todas"),d(),s(31,"mat-option",12),c(32,"Bot"),d(),s(33,"mat-option",13),c(34,"Conversa"),d(),s(35,"mat-option",14),c(36,"Mensagem"),d(),s(37,"mat-option",15),c(38,"Usu\xE1rio"),d()()(),s(39,"mat-form-field",5)(40,"mat-label"),c(41,"Status"),d(),s(42,"mat-select",6),z("ngModelChange",function(o){return H(a.filterStatus,o)||(a.filterStatus=o),o}),g("change",function(){return a.loadAuditLogs()}),s(43,"mat-option",7),c(44,"Todos"),d(),s(45,"mat-option",16),c(46,"Sucesso"),d(),s(47,"mat-option",17),c(48,"Falha"),d()()()()()(),s(49,"mat-card",18)(50,"mat-card-content"),E(51,cn,5,1,"div",19),E(52,pn,3,0,"div",20),E(53,un,5,0,"div",21),E(54,In,27,3,"table",22),E(55,Tn,1,4,"mat-paginator",23),d()()()),t&2&&(l(14),B("ngModel",a.filterAction),l(14),B("ngModel",a.filterEntityType),l(14),B("ngModel",a.filterStatus),l(9),x(a.error?51:-1),l(),x(a.loading?52:-1),l(),x(!a.loading&&a.auditLogs.length===0?53:-1),l(),x(!a.loading&&a.auditLogs.length>0?54:-1),l(),x(!a.loading&&a.auditLogs.length>0?55:-1))},dependencies:[gt,aa,qt,$t,Xt,Qt,Ut,Jt,Gt,Zt,ea,ta,ua,pa,ve,re,ie,Ot,Rt,Yt,Pt,Nt,Kt,jt,wa,Aa,ia,Wt,zt,ra,la,da,sa,yt,Dt,vt,wt,_t],styles:[".container[_ngcontent-%COMP%]{padding:20px}.header[_ngcontent-%COMP%]{display:flex;justify-content:space-between;align-items:center;margin-bottom:20px}.header[_ngcontent-%COMP%]   h1[_ngcontent-%COMP%]{margin:0}.filters-card[_ngcontent-%COMP%]{margin-bottom:20px}.filters-card[_ngcontent-%COMP%]   mat-card-content[_ngcontent-%COMP%]{padding:16px}.filters-grid[_ngcontent-%COMP%]{display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:16px}.logs-card[_ngcontent-%COMP%]   mat-card-content[_ngcontent-%COMP%]{padding:0}.loading-state[_ngcontent-%COMP%]{padding:24px;text-align:center;color:#64748b}.full-width-table[_ngcontent-%COMP%]{width:100%;border-collapse:collapse}.full-width-table[_ngcontent-%COMP%]   th[_ngcontent-%COMP%]{background-color:#f5f5f5;font-weight:600}.full-width-table[_ngcontent-%COMP%]   td[_ngcontent-%COMP%]{padding:12px;border-bottom:1px solid #ddd}.full-width-table[_ngcontent-%COMP%]   tr[_ngcontent-%COMP%]:hover{background-color:#fafafa}mat-chip[_ngcontent-%COMP%]{cursor:default;font-size:12px}mat-paginator[_ngcontent-%COMP%]{border-top:1px solid #ddd}button[_ngcontent-%COMP%]{margin-right:8px}.state-card[_ngcontent-%COMP%]   p[_ngcontent-%COMP%]{margin:8px 0 0}"]})}return n})();export{cr as AuditLogsComponent};
