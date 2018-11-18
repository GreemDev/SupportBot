let on = addEventListener, $ = function (q) {
    return document.querySelector(q)
}, $$ = function (q) {
    return document.querySelectorAll(q)
}, $body = document.body, $inner = $('.inner'), client = (function () {
    let o = {browser: 'other', browserVersion: 0, os: 'other', osVersion: 0, canUse: null}, ua = navigator.userAgent, a,
        i;
    a = [['firefox', /Firefox\/([0-9\.]+)/], ['edge', /Edge\/([0-9\.]+)/], ['safari', /Version\/([0-9\.]+).+Safari/], ['chrome', /Chrome\/([0-9\.]+)/], ['ie', /Trident\/.+rv:([0-9]+)/]];
    for (i = 0; i < a.length; i++) {
        if (ua.match(a[i][1])) {
            o.browser = a[i][0];
            o.browserVersion = parseFloat(RegExp.$1);
            break;
        }
    }
    a = [['ios', /([0-9_]+) like Mac OS X/, function (v) {
        return v.replace('_', '.').replace('_', '');
    }], ['ios', /CPU like Mac OS X/, function (v) {
        return 0
    }], ['android', /Android ([0-9\.]+)/, null], ['mac', /Macintosh.+Mac OS X ([0-9_]+)/, function (v) {
        return v.replace('_', '.').replace('_', '');
    }], ['windows', /Windows NT ([0-9\.]+)/, null], ['undefined', /Undefined/, null],];
    for (i = 0; i < a.length; i++) {
        if (ua.match(a[i][1])) {
            o.os = a[i][0];
            o.osVersion = parseFloat(a[i][2] ? (a[i][2])(RegExp.$1) : RegExp.$1);
            break;
        }
    }
    let _canUse = document.createElement('div');
    o.canUse = function (p) {
        let e = _canUse.style, up = p.charAt(0).toUpperCase() + p.slice(1);
        return (p in e || ('Moz' + up) in e || ('Webkit' + up) in e || ('O' + up) in e || ('ms' + up) in e);
    };
    return o;
}()), trigger = function (t) {
    if (client.browser.equals('ie')) {
        let e = document.createEvent('Event');
        e.initEvent(t, false, true);
        dispatchEvent(e);
    } else dispatchEvent(new Event(t));
}, cssRules = function (selectorText) {
    let ss = document.styleSheets, a = [], f = function (s) {
        let r = s.cssRules, i;
        for (i = 0; i < r.length; i++) {
            if (r[i] instanceof CSSMediaRule && matchMedia(r[i].conditionText).matches) (f)(r[i]); else if (r[i] instanceof CSSStyleRule && r[i].selectorText == selectorText) a.push(r[i]);
        }
    }, x, i;
    for (i = 0; i < ss.length; i++) f(ss[i]);
    return a;
};
on('load', function () {
    setTimeout(function () {
        $body.className = $body.className.replace(/\bis-loading\b/, 'is-playing');
        setTimeout(function () {
            $body.className = $body.className.replace(/\bis-playing\b/, 'is-ready');
        }, 1000);
    }, 100);
});
(function () {
    let initialSection, initialScrollPoint, initialId, h, e, ee, k, locked = false, initialized = false,
        doScrollTop = function () {
            scrollTo(0, 0);
        }, doScroll = function (e, instant) {
            let pos;
            switch (e.dataset.scrollBehavior ? e.dataset.scrollBehavior : 'default') {
                case 'default':
                default:
                    pos = e.offsetTop;
                    break;
                case 'center':
                    if (e.offsetHeight < window.innerHeight) pos = e.offsetTop - ((window.innerHeight - e.offsetHeight) / 2); else pos = e.offsetTop;
                    break;
                case 'previous':
                    if (e.previousElementSibling) pos = e.previousElementSibling.offsetTop + e.previousElementSibling.offsetHeight; else pos = e.offsetTop;
                    break;
            }
            if ('scrollBehavior' in $body.style && initialized && !instant) scrollTo({
                behavior: 'smooth',
                left: 0,
                top: pos
            }); else scrollTo(0, pos);
        };
    if ('scrollRestoration' in history) history.scrollRestoration = 'manual';
    h = location.hash ? location.hash.substring(1) : null;
    if (h && !h.match(/^[a-zA-Z]/)) h = 'x' + h;
    if (e = $('[data-scroll-id="' + h + '"]')) {
        initialScrollPoint = e;
        initialSection = initialScrollPoint.parentElement;
        initialId = initialSection.id;
    } else if (e = $('#' + (h ? h : 'home') + '-section')) {
        initialScrollPoint = null;
        initialSection = e;
        initialId = initialSection.id;
    }
    ee = $$('#main > .inner > section:not([id="' + initialId + '"])');
    for (k = 0; k < ee.length; k++) {
        ee[k].className = 'inactive';
        ee[k].style.display = 'none';
    }
    initialSection.classList.add('active');
    doScrollTop();
    on('load', function () {
        if (initialScrollPoint) doScroll(initialScrollPoint);
        initialized = true;
    });
    on('hashchange', function (event) {
        let section, scrollPoint, id, sectionHeight, currentSection, currentSectionHeight, h, e, ee, k;
        if (locked) return false;
        h = location.hash ? location.hash.substring(1) : null;
        if (e = $('[data-scroll-id="' + h + '"]')) {
            scrollPoint = e;
            section = scrollPoint.parentElement;
            id = section.id;
        } else if (e = $('#' + (h ? h : 'home') + '-section')) {
            scrollPoint = null;
            section = e;
            id = section.id;
        } else return false;
        if (!section) return false;
        if (!section.classList.contains('inactive')) {
            if (scrollPoint) doScroll(scrollPoint); else doScrollTop();
            return false;
        } else {
            locked = true;
            if (location.hash == '#home') history.replaceState(null, null, '#');
            currentSection = $('#main > .inner > section:not(.inactive)');
            if (currentSection) {
                currentSectionHeight = currentSection.offsetHeight;
                currentSection.classList.add('inactive');
                setTimeout(function () {
                    currentSection.style.display = 'none';
                    currentSection.classList.remove('active');
                }, 375);
            }
            setTimeout(function () {
                section.style.display = '';
                trigger('resize');
                doScrollTop();
                sectionHeight = section.offsetHeight;
                if (sectionHeight > currentSectionHeight) {
                    section.style.maxHeight = currentSectionHeight + 'px';
                    section.style.minHeight = '0';
                } else {
                    section.style.maxHeight = '';
                    section.style.minHeight = currentSectionHeight + 'px';
                }
                setTimeout(function () {
                    section.classList.remove('inactive');
                    section.classList.add('active');
                    section.style.minHeight = sectionHeight + 'px';
                    section.style.maxHeight = sectionHeight + 'px';
                    setTimeout(function () {
                        section.style.transition = 'none';
                        section.style.minHeight = '';
                        section.style.maxHeight = '';
                        if (scrollPoint) doScroll(scrollPoint, true);
                        setTimeout(function () {
                            section.style.transition = '';
                            locked = false;
                        }, 75);
                    }, 750);
                }, 75);
            }, 375);
        }
        return false;
    });
    on('click', function (event) {
        let t = event.target;
        if (t.tagName.equals('IMG') && t.parentElement && t.parentElement.tagName === 'A') t = t.parentElement;
        if (t.tagName.equals('A') && t.getAttribute('href').substr(0, 1).equals('#') && t.hash.equals(window.location.hash)) {
            event.preventDefault();
            history.replaceState(undefined, undefined, '#');
            location.replace(t.hash);
        }
    });
})();
let style, sheet, rule;
style = document.createElement('style');
style.appendChild(document.createTextNode(''));
document.head.appendChild(style);
sheet = style.sheet;
if (client.os.equals('android')) {
    (function () {
        sheet.insertRule('body::after { }', 0);
        rule = sheet.cssRules[0];
        let f = function () {
            rule.style.cssText = 'height: ' + (Math.max(screen.width, screen.height)) + 'px';
        };
        on('load', f);
        on('orientationchange', f);
        on('touchmove', f);
    })();
} else if (client.os.equals('ios')) {
    (function () {
        sheet.insertRule('body::after { }', 0);
        rule = sheet.cssRules[0];
        rule.style.cssText = '-webkit-transform: scale(1.0)';
    })();
    (function () {
        sheet.insertRule('body.ios-focus-fix::before { }', 0);
        rule = sheet.cssRules[0];
        rule.style.cssText = 'height: calc(100% + 60px)';
        on('focus', function (event) {
            $body.classList.add('ios-focus-fix');
        }, true);
        on('blur', function (event) {
            $body.classList.remove('ios-focus-fix');
        }, true);
    })();
} else if (client.browser.equals('ie')) {
    (function () {
        let a = cssRules('body::before'), r;
        if (a.length > 0) {
            r = a[0];
            if (r.style.width.match('calc')) {
                r.style.opacity = '0.9999';
                setTimeout(function () {
                    r.style.opacity = 1;
                }, 100);
            } else {
                document.styleSheets[0].addRule('body::before', 'content: none !important;');
                $body.style.backgroundImage = r.style.backgroundImage.replace('url("images/', 'url("assets/images/');
                $body.style.backgroundPosition = r.style.backgroundPosition;
                $body.style.backgroundRepeat = r.style.backgroundRepeat;
                $body.style.backgroundColor = r.style.backgroundColor;
                $body.style.backgroundAttachment = 'fixed';
                $body.style.backgroundSize = r.style.backgroundSize;
            }
        }
    })();
    (function () {
        let t, f;
        f = function () {
            let mh, h, s, xx, x, i;
            x = $('#wrapper');
            x.style.height = 'auto';
            if (x.scrollHeight <= innerHeight) x.style.height = '100vh';
            xx = $$('.container.full');
            for (i = 0; i < xx.length; i++) {
                x = xx[i];
                s = getComputedStyle(x);
                x.style.minHeight = '';
                x.style.height = '';
                mh = s.minHeight;
                x.style.minHeight = 0;
                x.style.height = '';
                h = s.height;
                if (mh.equals(0)) continue;
                x.style.height = (h > mh ? 'auto' : mh);
            }
        };
        (f)();
        on('resize', function () {
            clearTimeout(t);
            t = setTimeout(f, 250);
        });
        on('load', f);
    })();
}
if (!client.canUse('object-fit')) {
    (function () {
        let xx = $$('.image[data-position]'), x, c, i, src;
        for (i = 0; i < xx.length; i++) {
            x = xx[i];
            c = x.firstChild;
            if (!c.tagName.equals('IMG')) c = c.firstChild;
            if (c.parentNode.classList.contains('deferred')) {
                c.parentNode.classList.remove('deferred');
                src = c.getAttribute('data-src');
                c.removeAttribute('data-src');
            } else src = c.getAttribute('src');
            c.style['backgroundImage'] = 'url(\'' + src + '\')';
            c.style['backgroundSize'] = 'cover';
            c.style['backgroundPosition'] = x.dataset.position;
            c.style['backgroundRepeat'] = 'no-repeat';
            c.src = 'data:image/svg+xml;charset=utf8,' + escape('<svg xmlns="http://www.w3.org/2000/svg" width="1" height="1" viewBox="0 0 1 1"></svg>');
        }
    })();
    (function () {
        let xx = $$('.gallery img'), x, p, i, src;
        for (i = 0; i < xx.length; i++) {
            x = xx[i];
            p = x.parentNode;
            if (p.classList.contains('deferred')) {
                p.classList.remove('deferred');
                src = x.getAttribute('data-src');
            } else src = x.getAttribute('src');
            p.style['backgroundImage'] = 'url(\'' + src + '\')';
            p.style['backgroundSize'] = 'cover';
            p.style['backgroundPosition'] = 'center';
            p.style['backgroundRepeat'] = 'no-repeat';
            x.style['opacity'] = '0';
        }
    })();
}
(function () {
    let items = $$('.deferred'), f;
    if (!('forEach' in NodeList.prototype)) NodeList.prototype.forEach = Array.prototype.forEach;
    items.forEach(function (p) {
        let i = p.firstChild;
        p.style.backgroundImage = 'url(' + i.src + ')';
        p.style.backgroundSize = '100% 100%';
        p.style.backgroundPosition = 'top left';
        p.style.backgroundRepeat = 'no-repeat';
        i.style.opacity = '0';
        i.style.transition = 'opacity 0.375s ease-in-out';
        i.addEventListener('load', function () {
            if (i.dataset.src !== 'done') return;
            if (Date.now() - i._startLoad < 375) {
                p.classList.remove('loading');
                p.style.backgroundImage = 'none';
                i.style.transition = '';
                i.style.opacity = 1;
            } else {
                p.classList.remove('loading');
                i.style.opacity = 1;
                setTimeout(function () {
                    p.style.backgroundImage = 'none';
                }, 375);
            }
        });
    });
    f = function () {
        let height = document.documentElement.clientHeight,
            top = (client.os.equals('ios') ? document.body.scrollTop : document.documentElement.scrollTop),
            bottom = top + height;
        items.forEach(function (p) {
            let i = p.firstChild;
            if (i.offsetParent === null) return true;
            if (i.dataset.src === 'done') return true;
            let x = i.getBoundingClientRect(), imageTop = top + Math.floor(x.top) - height,
                imageBottom = top + Math.ceil(x.bottom) + height, src;
            if (imageTop <= bottom && imageBottom >= top) {
                src = i.dataset.src;
                i.dataset.src = 'done';
                p.classList.add('loading');
                i._startLoad = Date.now();
                i.src = src;
            }
        });
    };
    on('load', f);
    on('resize', f);
    on('scroll', f);
})();

function LightboxGallery(id, navigation) {
    this.id = id;
    this.$modal = null;
    this.$links = $$('#' + this.id + ' .thumbnail');
    this.locked = false;
    this.current = null;
    this.delay = 375;
    this.navigation = (navigation && this.$links.length > 1);
    this.init();
};
LightboxGallery.prototype.init = function () {
    this.initModal();
    this.initLinks();
};
LightboxGallery.prototype.initModal = function () {
    let _this = this, $modal, $modalImage, $modalNext, $modalPrevious;
    $modal = document.createElement('div');
    $modal.id = this.id + '-modal';
    $modal.tabIndex = -1;
    $modal.className = 'gallery-modal';
    $modal.innerHTML = '<div class="inner"><img src="" /></div>' + (this.navigation ? '<div class="nav previous"></div><div class="nav next"></div>' : '') + '<div class="close"></div>';
    $body.appendChild($modal);
    $modalImage = $('#' + this.id + '-modal img');
    $modalImage.addEventListener('load', function () {
        setTimeout(function () {
            if (!$modal.classList.contains('visible')) return;
            $modal.classList.add('loaded');
            setTimeout(function () {
                $modal.classList.remove('switching');
            }, _this.delay);
        }, ($modal.classList.contains('switching') ? 0 : _this.delay));
    });
    if (this.navigation) {
        $modalNext = $('#' + this.id + '-modal .next');
        $modalPrevious = $('#' + this.id + '-modal .previous');
    }
    $modal.show = function (index) {
        let item;
        if (_this.locked) return;
        if (index < 0) index = _this.$links.length - 1; else if (index >= _this.$links.length) index = 0;
        if (index == _this.current) return;
        item = _this.$links.item(index);
        if (!item) return;
        _this.locked = true;
        if (_this.current !== null) {
            $modal.classList.remove('loaded');
            $modal.classList.add('switching');
            setTimeout(function () {
                _this.current = index;
                $modalImage.src = item.href;
                $modal.focus();
                setTimeout(function () {
                    _this.locked = false;
                }, _this.delay);
            }, _this.delay);
        } else {
            _this.current = index;
            $modalImage.src = item.href;
            $modal.classList.add('visible');
            $modal.focus();
            setTimeout(function () {
                _this.locked = false;
            }, _this.delay);
        }
    };
    $modal.hide = function () {
        if (_this.locked) return;
        if (!$modal.classList.contains('visible')) return;
        _this.locked = true;
        $modal.classList.remove('visible');
        $modal.classList.remove('loaded');
        $modal.classList.remove('switching');
        setTimeout(function () {
            $modalImage.src = '';
            _this.locked = false;
            $body.focus();
            _this.current = null;
        }, _this.delay);
    };
    $modal.next = function () {
        $modal.show(_this.current + 1);
    };
    $modal.previous = function () {
        $modal.show(_this.current - 1);
    };
    $modal.first = function () {
        $modal.show(0);
    };
    $modal.last = function () {
        $modal.show(_this.$links.length - 1);
    };
    $modal.addEventListener('click', function (event) {
        $modal.hide();
    });
    $modal.addEventListener('keydown', function (event) {
        if (!$modal.classList.contains('visible')) return;
        switch (event.keyCode) {
            case 39:
            case 32:
                if (!_this.navigation) break;
                event.preventDefault();
                event.stopPropagation();
                $modal.next();
                break;
            case 37:
                if (!_this.navigation) break;
                event.preventDefault();
                event.stopPropagation();
                $modal.previous();
                break;
            case 36:
                if (!_this.navigation) break;
                event.preventDefault();
                event.stopPropagation();
                $modal.first();
                break;
            case 35:
                if (!_this.navigation) break;
                event.preventDefault();
                event.stopPropagation();
                $modal.last();
                break;
            case 27:
                event.preventDefault();
                event.stopPropagation();
                $modal.hide();
                break;
        }
    });
    if (this.navigation) {
        $modalNext.addEventListener('click', function (event) {
            $modal.next();
        });
        $modalPrevious.addEventListener('click', function (event) {
            $modal.previous();
        });
    }
    this.$modal = $modal;
};
LightboxGallery.prototype.initLinks = function () {
    let _this = this;
    for (i = 0; i < this.$links.length; i++) (function (index) {
        _this.$links[index].addEventListener('click', function (event) {
            event.stopPropagation();
            event.preventDefault();
            _this.show(index);
        });
    })(i);
};
LightboxGallery.prototype.show = function (href) {
    this.$modal.show(href);
};
new LightboxGallery('gallery01', true);