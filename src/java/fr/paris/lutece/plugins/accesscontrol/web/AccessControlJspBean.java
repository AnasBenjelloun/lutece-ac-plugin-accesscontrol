/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
 	
package fr.paris.lutece.plugins.accesscontrol.web;

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControl;
import fr.paris.lutece.plugins.accesscontrol.business.AccessControlHome;

/**
 * This class provides the user interface to manage AccessControl features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageAccessControls.jsp", controllerPath = "jsp/admin/plugins/accesscontrol/", right = "ACCESSCONTROL_MANAGEMENT" )
public class AccessControlJspBean extends AbstractManageAccessControlJspBean
{
    private static final long serialVersionUID = 128971112958212947L;
    
    // Templates
    private static final String TEMPLATE_MANAGE_ACCESSCONTROLS = "/admin/plugins/accesscontrol/manage_accesscontrols.html";
    private static final String TEMPLATE_CREATE_ACCESSCONTROL = "/admin/plugins/accesscontrol/create_accesscontrol.html";
    private static final String TEMPLATE_MODIFY_ACCESSCONTROL = "/admin/plugins/accesscontrol/modify_accesscontrol.html";

    // Parameters
    private static final String PARAMETER_ID_ACCESSCONTROL = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ACCESSCONTROLS = "accesscontrol.manage_accesscontrols.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ACCESSCONTROL = "accesscontrol.modify_accesscontrol.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_ACCESSCONTROL = "accesscontrol.create_accesscontrol.pageTitle";

    // Markers
    private static final String MARK_ACCESSCONTROL_LIST = "accesscontrol_list";
    private static final String MARK_ACCESSCONTROL = "accesscontrol";

    private static final String JSP_MANAGE_ACCESSCONTROLS = "jsp/admin/plugins/accesscontrol/ManageAccessControls.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_ACCESSCONTROL = "accesscontrol.message.confirmRemoveAccessControl";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "accesscontrol.model.entity.accesscontrol.attribute.";

    // Views
    private static final String VIEW_MANAGE_ACCESSCONTROLS = "manageAccessControls";
    private static final String VIEW_CREATE_ACCESSCONTROL = "createAccessControl";
    private static final String VIEW_MODIFY_ACCESSCONTROL = "modifyAccessControl";

    // Actions
    private static final String ACTION_CREATE_ACCESSCONTROL = "createAccessControl";
    private static final String ACTION_MODIFY_ACCESSCONTROL = "modifyAccessControl";
    private static final String ACTION_REMOVE_ACCESSCONTROL = "removeAccessControl";
    private static final String ACTION_CONFIRM_REMOVE_ACCESSCONTROL = "confirmRemoveAccessControl";
    private static final String ACTION_ENABLE_ACCESSCONTROL = "enableAccessControl";
    private static final String ACTION_DISABLE_ACCESSCONTROL = "disableAccessControl";
    
    // Infos
    private static final String INFO_ACCESSCONTROL_CREATED = "accesscontrol.info.accesscontrol.created";
    private static final String INFO_ACCESSCONTROL_UPDATED = "accesscontrol.info.accesscontrol.updated";
    private static final String INFO_ACCESSCONTROL_REMOVED = "accesscontrol.info.accesscontrol.removed";
    
    // Session variable to store working values
    private AccessControl _accesControl;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_ACCESSCONTROLS, defaultView = true )
    public String getManageAccessControls( HttpServletRequest request )
    {
        _accesControl = null;
        List<AccessControl> listAccessControls = AccessControlHome.getAccessControlsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_ACCESSCONTROL_LIST, listAccessControls, JSP_MANAGE_ACCESSCONTROLS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_ACCESSCONTROLS, TEMPLATE_MANAGE_ACCESSCONTROLS, model );
    }

    /**
     * Returns the form to create a accesscontrol
     *
     * @param request The Http request
     * @return the html code of the accesscontrol form
     */
    @View( VIEW_CREATE_ACCESSCONTROL )
    public String getCreateAccessControl( HttpServletRequest request )
    {
        _accesControl = ( _accesControl != null ) ? _accesControl : new AccessControl(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_ACCESSCONTROL, _accesControl );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_ACCESSCONTROL ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_ACCESSCONTROL, TEMPLATE_CREATE_ACCESSCONTROL, model );
    }

    /**
     * Process the data capture form of a new accesscontrol
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_ACCESSCONTROL )
    public String doCreateAccessControl( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _accesControl, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_ACCESSCONTROL ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _accesControl, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_ACCESSCONTROL );
        }

        AccessControlHome.create( _accesControl );
        addInfo( INFO_ACCESSCONTROL_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }

    /**
     * Manages the removal form of a accesscontrol whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_ACCESSCONTROL )
    public String getConfirmRemoveAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_ACCESSCONTROL ) );
        url.addParameter( PARAMETER_ID_ACCESSCONTROL, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ACCESSCONTROL, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a accesscontrol
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage accesscontrols
     */
    @Action( ACTION_REMOVE_ACCESSCONTROL )
    public String doRemoveAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );
        AccessControlHome.remove( nId );
        addInfo( INFO_ACCESSCONTROL_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }

    /**
     * Returns the form to update info about a accesscontrol
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_ACCESSCONTROL )
    public String getModifyAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );

        if ( _accesControl == null || ( _accesControl.getId(  ) != nId ) )
        {
            _accesControl = AccessControlHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_ACCESSCONTROL, _accesControl );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_ACCESSCONTROL ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_ACCESSCONTROL, TEMPLATE_MODIFY_ACCESSCONTROL, model );
    }

    /**
     * Process the change form of a accesscontrol
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_ACCESSCONTROL )
    public String doModifyAccessControl( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _accesControl, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_ACCESSCONTROL ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _accesControl, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_ACCESSCONTROL, PARAMETER_ID_ACCESSCONTROL, _accesControl.getId( ) );
        }

        AccessControlHome.update( _accesControl );
        addInfo( INFO_ACCESSCONTROL_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }
    
    /**
     * Enables the accesscontrol
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_ENABLE_ACCESSCONTROL )
    public String doEnableAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );
        
        AccessControl accessControl = AccessControlHome.findByPrimaryKey( nId );
        if ( accessControl != null )
        {
            accessControl.setEnabled( true );
            AccessControlHome.update( accessControl );
        }
        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }
    
    /**
     * Disables the accesscontrol
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_DISABLE_ACCESSCONTROL )
    public String doDisableAccessControl( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ACCESSCONTROL ) );
        
        AccessControl accessControl = AccessControlHome.findByPrimaryKey( nId );
        if ( accessControl != null )
        {
            accessControl.setEnabled( false );
            AccessControlHome.update( accessControl );
        }
        return redirectView( request, VIEW_MANAGE_ACCESSCONTROLS );
    }
}