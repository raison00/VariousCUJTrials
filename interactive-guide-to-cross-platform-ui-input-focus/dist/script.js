const uiComponentData = [
            {
                component: "Text Field (Input)",
                standard: "Enters text and handles text selection.",
                focus: "Tab Key moves focus sequentially to the field. Accessibility: Highlighted focus ring/border.",
                desktop: { title: "Desktop (Mouse)", content: "Single-click to place cursor. Double-click to select word. Triple-click to select line/paragraph." },
                mac: { title: "Mac Trackpad/Touchpad", content: "Tap to place cursor. Force Touch/Deep Press on selection often opens dictionary/data detectors. Light haptic click on initial tap." },
                android: { title: "Android (Touchscreen)", content: "Single-tap opens software keyboard and places cursor. Field highlights; active cursor blinks. Long-press triggers selection/contextual menu (Cut/Copy/Paste)." }
            },
            {
                component: "Button (Action)",
                standard: "Triggers a function or event immediately.",
                focus: "Tab Key moves focus to the button. Enter/Space Key activates the button.",
                desktop: { title: "Desktop (Mouse)", content: "`mousedown` initiates press state; `mouseup` initiates click event. Hover changes color/shadow to indicate interactivity." },
                mac: { title: "Mac Trackpad/Touchpad", content: "Single-finger tap initiates click. Hover relies on cursor presence for visual state change." },
                android: { title: "Android (Touchscreen)", content: "Single-tap triggers event. Press State shows a visual ripple effect or darkened background. No Hover State." }
            },
            {
                component: "Slider (Range Input)",
                standard: "Adjusts a value by moving a handle along a track.",
                focus: "Tab Key moves focus to the handle. Arrow Keys increment/decrement the value.",
                desktop: { title: "Desktop (Mouse)", content: "Click and drag the thumb/handle to change the value." },
                mac: { title: "Mac Trackpad/Touchpad", content: "Click and drag (requires a continuous downward pressure). Multi-touch scrolling over the slider might override the drag." },
                android: { title: "Android (Touchscreen)", content: "Tap-and-drag the thumb. Requires a large touch target size for usability." }
            },
            {
                component: "Scrollable Content",
                standard: "Navigates content that exceeds the viewport.",
                focus: "Tab Key focuses on the container or elements within it. Page Up/Down and Arrow keys scroll the view.",
                desktop: { title: "Desktop (Mouse)", content: "Scroll wheel or middle button drag for vertical/horizontal scrolling." },
                mac: { title: "Mac Trackpad/Touchpad", content: "Two-finger drag for smooth, momentum-based scrolling. Pinch gesture for zooming if enabled." },
                android: { title: "Android (Touchscreen)", content: "Single-finger swipe/flick generates momentum-based scrolling. Pull-to-Refresh is a common specific swipe gesture." }
            },
            {
                component: "Dropdown/Select",
                standard: "Opens a list of options for selection.",
                focus: "Tab Key focuses the control. Enter/Space Key opens the menu. Arrow Keys navigate options.",
                desktop: { title: "Desktop (Mouse)", content: "Single-click opens list. Selection requires a second click." },
                mac: { title: "Mac Trackpad/Touchpad", content: "Single-tap opens a list, often using a system-level menu/picker UI." },
                android: { title: "Android (Touchscreen)", content: "Single-tap opens a system modal or bottom sheet picker, often covering the entire screen." }
            },
            {
                component: "Drag-and-Drop (Reorder)",
                standard: "Spatially moves an element to a new location.",
                focus: "No standard Tab interaction. Typically requires keyboard shortcuts or context menus for accessibility.",
                desktop: { title: "Desktop (Mouse)", content: "`mousedown` + `mousemove` until `mouseup`. Visual feedback includes cursor change and a drag ghost image." },
                mac: { title: "Mac Trackpad/Touchpad", content: "Click, hold, and drag with one finger. May require a multi-finger trigger (like three-finger drag) for system-level dragging." },
                android: { title: "Android (Touchscreen)", content: "Long Press initiates the drag state. Lift finger and release over the target zone. Visual feedback usually involves scaling up the dragged element." }
            },
            {
                component: "Context Menu",
                standard: "Accesses secondary, contextual actions for an element.",
                focus: "Shift+F10 or Context Menu Key opens the menu for the focused element.",
                desktop: { title: "Desktop (Mouse)", content: "Right-click (secondary mouse button)." },
                mac: { title: "Mac Trackpad/Touchpad", content: "Two-finger tap (default secondary click). A deep press can also be used." },
                android: { title: "Android (Touchscreen)", content: "Long Press and Hold on the element triggers the contextual menu (often a floating pop-up)." }
            },
            {
                component: "Hover State (Informational)",
                standard: "Provides passive information or visual cue without a click.",
                focus: "Not applicable. Hover is a pointer-specific state.",
                desktop: { title: "Desktop (Mouse)", content: "Cursor moves over the target area. A tooltip may appear, or a button might change its appearance." },
                mac: { title: "Mac Trackpad/Touchpad", content: "Requires the cursor to be over the element. Generally less impactful in Mac UIs than Windows, but still common." },
                android: { title: "Android (Touchscreen)", content: "Non-existent. The closest analog is a brief press state, but a true hover state cannot occur on a standard touchscreen." }
            }
        ];
        
        const platformColors = {
            desktop: 'border-l-sky-500',
            mac: 'border-l-purple-500',
            android: 'border-l-green-500',
            default: 'border-l-gray-300'
        };

        let currentPlatformFilter = 'all';
        let currentComponentFilter = 'all';

        const componentGrid = document.getElementById('component-grid');
        const platformFilters = document.getElementById('platform-filters');
        const componentSelect = document.getElementById('component-select');
        
        function renderComponents() {
            componentGrid.innerHTML = '';
            const filteredData = uiComponentData.filter(item => 
                currentComponentFilter === 'all' || item.component === currentComponentFilter
            );

            filteredData.forEach((item, index) => {
                const isVisible = currentPlatformFilter === 'all';
                const highlightDesktop = currentPlatformFilter === 'desktop';
                const highlightMac = currentPlatformFilter === 'mac';
                const highlightAndroid = currentPlatformFilter === 'android';

                const card = `
                <div class="bg-white rounded-xl shadow-sm overflow-hidden">
                    <div class="accordion-header p-5 cursor-pointer flex justify-between items-center hover:bg-gray-50" data-index="${index}">
                        <h3 class="text-xl font-semibold text-gray-800">${item.component}</h3>
                        <div class="icon">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7" />
                            </svg>
                        </div>
                    </div>
                    <div class="accordion-content px-5 pb-5">
                        <div class="mb-4 p-4 bg-gray-50 rounded-lg border">
                            <p class="font-semibold text-gray-700">Standard Interaction:</p>
                            <p class="text-gray-600">${item.standard}</p>
                            <p class="font-semibold text-gray-700 mt-2">Focus Mechanism:</p>
                            <p class="text-gray-600">${item.focus}</p>
                        </div>
                        <div class="grid sm:grid-cols-1 md:grid-cols-3 gap-4">
                            <div class="platform-card p-4 rounded-lg bg-gray-50/50 ${platformColors.default} ${isVisible ? 'opacity-100' : (highlightDesktop ? 'opacity-100' : 'opacity-50')} ${highlightDesktop ? `highlight ${platformColors.desktop}` : ''}">
                                <h4 class="font-bold text-gray-700">${item.desktop.title}</h4>
                                <p class="text-sm text-gray-600 mt-1">${item.desktop.content}</p>
                            </div>
                             <div class="platform-card p-4 rounded-lg bg-gray-50/50 ${platformColors.default} ${isVisible ? 'opacity-100' : (highlightMac ? 'opacity-100' : 'opacity-50')} ${highlightMac ? `highlight ${platformColors.mac}` : ''}">
                                <h4 class="font-bold text-gray-700">${item.mac.title}</h4>
                                <p class="text-sm text-gray-600 mt-1">${item.mac.content}</p>
                            </div>
                             <div class="platform-card p-4 rounded-lg bg-gray-50/50 ${platformColors.default} ${isVisible ? 'opacity-100' : (highlightAndroid ? 'opacity-100' : 'opacity-50')} ${highlightAndroid ? `highlight ${platformColors.android}` : ''}">
                                <h4 class="font-bold text-gray-700">${item.android.title}</h4>
                                <p class="text-sm text-gray-600 mt-1">${item.android.content}</p>
                            </div>
                        </div>
                    </div>
                </div>`;
                componentGrid.innerHTML += card;
            });
            addAccordionListeners();
        }
        
        function addAccordionListeners() {
            document.querySelectorAll('.accordion-header').forEach(header => {
                header.addEventListener('click', () => {
                    const content = header.nextElementSibling;
                    header.classList.toggle('open');
                    content.classList.toggle('show');
                });
            });
        }
        
        function populateComponentSelect() {
            uiComponentData.forEach(item => {
                const option = document.createElement('option');
                option.value = item.component;
                option.textContent = item.component;
                componentSelect.appendChild(option);
            });
        }
        
        platformFilters.addEventListener('click', (e) => {
            if (e.target.tagName === 'BUTTON') {
                currentPlatformFilter = e.target.dataset.filter;
                platformFilters.querySelectorAll('button').forEach(btn => btn.classList.remove('active'));
                e.target.classList.add('active');
                renderComponents();
            }
        });

        componentSelect.addEventListener('change', (e) => {
            currentComponentFilter = e.target.value;
            renderComponents();
        });

        document.addEventListener('DOMContentLoaded', () => {
            populateComponentSelect();
            renderComponents();
        });